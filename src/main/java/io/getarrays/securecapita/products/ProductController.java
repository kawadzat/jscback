package io.getarrays.securecapita.products;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductsService productService;

    @PostMapping("/create")
    public ResponseEntity<Object> createProject(@RequestBody @Validated ProductDto productDto) {
        return productService.createProduct(productDto);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getProjects() {
        return productService.getProducts();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable(name = "id") Long id){
        return productService.deleteProduct(id);
    }
}
