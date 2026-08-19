package br.com.erudio.services;

import br.com.erudio.controllers.BookController;
import br.com.erudio.data.dto.v1.BookCreateDTO;
import br.com.erudio.data.dto.v1.BookDTO;
import br.com.erudio.mapper.ObjectMapper;
import br.com.erudio.model.Book;
import br.com.erudio.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookService {

    private Logger logger = LoggerFactory.getLogger(BookService.class.getName());

    @Autowired
    private BookRepository repository;

    @Autowired
    PagedResourcesAssembler<BookDTO> assembler;


    public BookDTO create(BookCreateDTO book) {
        logger.warn("Creating Book");

        Book up = ObjectMapper.parseObject(book, Book.class);


        return ObjectMapper.parseObject(repository.save(up), BookDTO.class);
    }

    public BookDTO update(BookCreateDTO book) {
        logger.warn("Updating Book");

        Book db = repository.findById(book.getId()).orElse(null);

        db.setAuthor(book.getAuthor());
        db.setLaunch_date(book.getLaunch_date());
        db.setPrice(book.getPrice());
        db.setTitle(book.getTitle());

        return ObjectMapper.parseObject(repository.save(db), BookDTO.class);
    }

    public BookDTO findById(Long id) {

        logger.warn("Finding Book");

        Book book = repository.findById(id).orElse(null);

        BookDTO result = ObjectMapper.parseObject(book, BookDTO.class);

        Hateoas(result);

        return result;
    }

    public PagedModel<EntityModel<BookDTO>> findAll(Pageable x) {
        logger.warn("Finding All");

        var books = repository.findAll(x);

        var result = books.map((y) -> {
            BookDTO dto = ObjectMapper.parseObject(y, BookDTO.class);
            Hateoas(dto);
            return dto;
        });

        Link findAllLink = linkTo(WebMvcLinkBuilder.methodOn(BookController.class)
                .findAll(x.getPageNumber(), x.getPageSize(), String.valueOf(x.getSort()))).withSelfRel();

        return assembler.toModel(result, findAllLink);
    }

    public void delete(Long id) {
        logger.warn("Deleting Bok");

        Book delete = repository.findById(id).orElse(null);

        repository.delete(delete);
    }

    public void Hateoas(BookDTO book) {
        book.add(linkTo(methodOn(BookController.class).findById(1L)).withSelfRel().withType("GET"));
        book.add(linkTo(methodOn(BookController.class).create(new BookCreateDTO())).withRel("create").withType("POST"));
        book.add(linkTo(methodOn(BookController.class).findAll(1,12,"asc")).withRel("findAll").withType("GET"));
        book.add(linkTo(methodOn(BookController.class).delete(1L)).withRel("delete").withType("DELETE"));
    }

}
