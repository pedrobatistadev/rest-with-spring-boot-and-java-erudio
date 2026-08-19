package br.com.erudio.controllers;

import br.com.erudio.controllers.docs.BookControllerDocs;
import br.com.erudio.data.dto.v1.BookCreateDTO;
import br.com.erudio.data.dto.v1.BookDTO;
import br.com.erudio.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/book")
public class BookController implements BookControllerDocs {

    @Autowired
    private BookService service;

    @Override
    public BookDTO create(@RequestBody BookCreateDTO book) {
        return service.create(book);
    }

    @Override
    public BookDTO update(@RequestBody BookCreateDTO book) {
        return service.update(book);
    }

    @Override
    public BookDTO findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @Override
    public ResponseEntity<PagedModel<EntityModel<BookDTO>>> findAll(@RequestParam(value = "page", defaultValue = "0")Integer page, @RequestParam(value = "size", defaultValue = "12") Integer size, @RequestParam(value = "direction", defaultValue = "asc") String direction) {
        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Direction.ASC, "id"));
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @Override
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        service.delete(id);

        return ResponseEntity.noContent().build();
    }

}
