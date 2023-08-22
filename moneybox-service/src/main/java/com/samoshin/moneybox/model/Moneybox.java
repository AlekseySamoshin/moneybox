package com.samoshin.moneybox.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "moneyboxes")
@Getter
@Setter
public class Moneybox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long sum;

    @OneToMany
    @JoinTable(name = "money_transfers",
            joinColumns = @JoinColumn(name = "moneybox_id"),
            inverseJoinColumns = @JoinColumn(name = "id"))
    private List<MoneyTransfer> transfers;
}
