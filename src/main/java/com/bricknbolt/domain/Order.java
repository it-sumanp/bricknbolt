package com.bricknbolt.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A Order.
 */
@Entity
@Table(name = "jhi_order")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "price", nullable = false)
    private Float price;

    @NotNull
    @Column(name = "order_date", nullable = false)
    private Instant orderDate;

    @Column(name = "medium")
    private String medium;

    @ManyToOne
    @JsonIgnoreProperties("orders")
    private BoltUser buyer;

    @ManyToOne
    @JsonIgnoreProperties("orders")
    private BoltUser referrer;

    @ManyToOne
    @JsonIgnoreProperties("orders")
    private Product item;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getPrice() {
        return price;
    }

    public Order price(Float price) {
        this.price = price;
        return this;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Instant getOrderDate() {
        return orderDate;
    }

    public Order orderDate(Instant orderDate) {
        this.orderDate = orderDate;
        return this;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
    }

    public String getMedium() {
        return medium;
    }

    public Order medium(String medium) {
        this.medium = medium;
        return this;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public BoltUser getBuyer() {
        return buyer;
    }

    public Order buyer(BoltUser boltUser) {
        this.buyer = boltUser;
        return this;
    }

    public void setBuyer(BoltUser boltUser) {
        this.buyer = boltUser;
    }

    public BoltUser getReferrer() {
        return referrer;
    }

    public Order referrer(BoltUser boltUser) {
        this.referrer = boltUser;
        return this;
    }

    public void setReferrer(BoltUser boltUser) {
        this.referrer = boltUser;
    }

    public Product getItem() {
        return item;
    }

    public Order item(Product product) {
        this.item = product;
        return this;
    }

    public void setItem(Product product) {
        this.item = product;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Order order = (Order) o;
        if (order.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), order.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Order{" +
            "id=" + getId() +
            ", price=" + getPrice() +
            ", orderDate='" + getOrderDate() + "'" +
            ", medium='" + getMedium() + "'" +
            "}";
    }
}
