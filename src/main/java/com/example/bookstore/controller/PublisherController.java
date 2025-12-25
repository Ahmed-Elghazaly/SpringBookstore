package com.example.bookstore.controller;

import com.example.bookstore.entity.Publisher;
import com.example.bookstore.repository.PublisherRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/publishers")
public class PublisherController {

    private final PublisherRepository publisherRepository;

    public PublisherController(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    /**
     * Get all publishers - used for dropdown in admin book creation form
     */
    @GetMapping
    public List<Publisher> getAllPublishers() {
        return publisherRepository.findAll();
    }

    /**
     * Add a new publisher - used by admin settings page
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Publisher createPublisher(@RequestBody Map<String, String> request) {
        String name = request.get("name");
        String address = request.get("address");
        String phoneNumber = request.get("phoneNumber");
        
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Publisher name is required");
        }
        
        Publisher publisher = new Publisher(name.trim(), address, phoneNumber);
        return publisherRepository.save(publisher);
    }
}
