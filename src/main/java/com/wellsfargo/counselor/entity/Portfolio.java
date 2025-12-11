package com.example.demo.entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "portfolios")
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Portfolio owns the client_id FK (1:1)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", unique = true, nullable = false)
    private Client client;

    @Column(nullable = false)
    private String name;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<SecurityHolding> holdings = new ArrayList<>();

    public Portfolio() {}

    public Portfolio(Long id, Client client, String name, LocalDateTime createdAt, LocalDateTime updatedAt, List<SecurityHolding> holdings) {
        this.id = id;
        this.client = client;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.holdings = holdings != null ? holdings : new ArrayList<>();
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /* Getters & setters */

    public Long getId() { return id; }
    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public List<SecurityHolding> getHoldings() { return holdings; }
    public void setHoldings(List<SecurityHolding> holdings) { this.holdings = holdings; }

    /* Convenience helpers */
    public void addHolding(SecurityHolding holding) {
        holdings.add(holding);
        holding.setPortfolio(this);
    }
    public void removeHolding(SecurityHolding holding) {
        holdings.remove(holding);
        holding.setPortfolio(null);
    }
}
