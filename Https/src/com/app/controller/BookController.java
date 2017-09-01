package com.app.controller;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.app.entity.Book;

@Path("/book")
public class BookController {

    public List<Book> getBooks() {
        List<Book> books = new ArrayList<Book>();

        books.add(new Book(1L, "Bopk1"));
        books.add(new Book(2L, "Bopk1"));
        books.add(new Book(3L, "Bopk1"));
        books.add(new Book(4L, "Bopk1"));

        return books;
    }

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Book> list() {
        return getBooks();
    }

}
