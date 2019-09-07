package com.bricknbolt.web.rest;

import com.bricknbolt.domain.BoltUser;
import com.bricknbolt.domain.Order;
import com.bricknbolt.domain.Product;
import com.bricknbolt.repository.BoltUserRepository;
import com.bricknbolt.repository.OrderRepository;
import com.bricknbolt.repository.ProductRepository;
import com.bricknbolt.web.rest.errors.BadRequestAlertException;
import com.bricknbolt.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Order.
 */
@RestController
@RequestMapping("/api")
public class OrderResource {

    private final Logger log = LoggerFactory.getLogger(OrderResource.class);

    private static final String ENTITY_NAME = "order";

    private final OrderRepository orderRepository;

    private final BoltUserRepository boltUserRepository;

    private final ProductRepository productRepository;

    public OrderResource(OrderRepository orderRepository, BoltUserRepository boltUserRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.boltUserRepository = boltUserRepository;
        this.productRepository = productRepository;
    }

    /**
     * POST  /orders : Create a new order.
     *
     * @param order the order to create
     * @return the ResponseEntity with status 201 (Created) and with body the new order, or with status 400 (Bad Request) if the order has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/orders")
    @Transactional
    public ResponseEntity<Order> createOrder(@Valid @RequestBody Order order) throws URISyntaxException {
        log.debug("REST request to save Order : {}", order);
        if (order.getId() != null) {
            throw new BadRequestAlertException("A new order cannot already have an ID", ENTITY_NAME, "idexists");
        }

        validateBuyerDetails(order);
        validateAffiliateDetails(order);
        validateProductDetails(order);

        BoltUser buyer = order.getBuyer();
        BoltUser savedBuyer = boltUserRepository.save(buyer);
        order.setBuyer(savedBuyer);

        Optional<BoltUser> referrer = boltUserRepository.findByPhone(order.getReferrer().getPhone());
        boolean isReferrerPresent = false;
        if(referrer.isPresent()) {
            order.setReferrer(referrer.get());
            isReferrerPresent = true;
        }else {
            order.setReferrer(null);
        }

        Optional<Product> productOptional = productRepository.findById(order.getItem().getId());
        Product product = productOptional.get();
        order.setItem(product);
        float orderPrice = product.getPrice();
        if(isReferrerPresent){
            orderPrice = orderPrice - 5000;
        }
        product.setPrice(orderPrice);

        Order result = orderRepository.save(order);
        return ResponseEntity.created(new URI("/api/orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    private void validateProductDetails(Order order) {
        Product product = order.getItem();
        if (product.getId() == null && product.getId() < 0) {
            throw new BadRequestAlertException("Product Is Empty", "BOLT_PRODUCT", "empty product");
        }
        Product productSaved = productRepository.getOne(product.getId());
        if (productSaved == null) {
            throw new BadRequestAlertException("Product Is Invalid", "BOLT_PRODUCT", "empty product");
        }
    }

    private void validateAffiliateDetails(Order order) {
        BoltUser affiliate = order.getReferrer();
        if (affiliate.getPhone() != null && !affiliate.getPhone().trim().equals("")) {
            Optional<BoltUser> ref = boltUserRepository.findByPhone(affiliate.getPhone().trim());
            if (!ref.isPresent()) {
                throw new BadRequestAlertException("Invalid Affiliate Details", "BOLT_ORDER", "Invalid Aff Id");
            }
        }

    }

    private void validateBuyerDetails(Order order) {
        BoltUser buyer = order.getBuyer();
        if (buyer.getName() == null && buyer.getName().trim().equals("")) {
            throw new BadRequestAlertException("Buyer Name Is Empty", "BOLT_USER", "empty buyer name");
        }

        if (buyer.getPhone() == null && buyer.getPhone().trim().equals("")) {
            throw new BadRequestAlertException("Buyer Phone Is Empty", "BOLT_USER", "empty buyer phone");
        }
    }

    /**
     * PUT  /orders : Updates an existing order.
     *
     * @param order the order to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated order,
     * or with status 400 (Bad Request) if the order is not valid,
     * or with status 500 (Internal Server Error) if the order couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/orders")
    public ResponseEntity<Order> updateOrder(@Valid @RequestBody Order order) throws URISyntaxException {
        log.debug("REST request to update Order : {}", order);
        if (order.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Order result = orderRepository.save(order);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, order.getId().toString()))
            .body(result);
    }

    /**
     * GET  /orders : get all the orders.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of orders in body
     */
    @GetMapping("/orders")
    public List<Order> getAllOrders() {
        log.debug("REST request to get all Orders");
        return orderRepository.findAll();
    }

    /**
     * GET  /orders/:id : get the "id" order.
     *
     * @param id the id of the order to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the order, or with status 404 (Not Found)
     */
    @GetMapping("/orders/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id) {
        log.debug("REST request to get Order : {}", id);
        Optional<Order> order = orderRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(order);
    }

    /**
     * DELETE  /orders/:id : delete the "id" order.
     *
     * @param id the id of the order to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/orders/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        log.debug("REST request to delete Order : {}", id);
        orderRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
