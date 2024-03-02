package com.example.appbooks.repo;

import com.example.appbooks.db.Book;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@ApplicationScoped
public class BookRepository {
    @PersistenceContext(unitName = "demo")
    private EntityManager em;


    public List<Book> findAll() {
        return this.em.createQuery("SELECT b FROM Book b", Book.class).getResultList();
    }

    public Book findById(Integer id) {
        return this.em.find(Book.class, id);
    }

    public Book create(Book instrument) {
        this.em.persist(instrument);
        return instrument;
    }

    public Book update(Book instrument) {
        return this.em.merge(instrument);
    }

    public void delete(Integer id) {
        Book instrument = this.em.find(Book.class, id);
        this.em.remove(instrument);
    }

}
