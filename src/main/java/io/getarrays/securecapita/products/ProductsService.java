package io.getarrays.securecapita.products;

import io.getarrays.securecapita.asserts.model.AssertEntity;
import io.getarrays.securecapita.exception.CustomMessage;
import io.getarrays.securecapita.roles.prerunner.ROLE_AUTH;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductsService {
    private final ProductRepository productRepository;

    public ResponseEntity<Object> createProduct(ProductDto productDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().anyMatch((r) -> r.getAuthority().contains(ROLE_AUTH.CREATE_PRODUCT.name()))) {
            Product product = productDto.toEntity();
//            Optional<Product> duplicatedProduct=productRepository.findByName(product.getName());
//            if(duplicatedProduct.isPresent()){
//                return ResponseEntity.status(422).body(new CustomMessage("Found Duplicate Entry. Please check again."));
//            }
            productRepository.save(product);
            return ResponseEntity.ok(new CustomMessage("Product Created Successfully."));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new CustomMessage("You don't have permission."));

    }

    public ResponseEntity<Object> getProducts() {
        return ResponseEntity.ok(ProductDto.toDtos(productRepository.findAll()));
    }

    public ResponseEntity<Object> deleteProduct(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().anyMatch((r) -> r.getAuthority().contains(ROLE_AUTH.CREATE_PRODUCT.name()))) {
            Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            productRepository.deleteById(id);
            return ResponseEntity.ok(new CustomMessage("deleted successfully."));
        }
        return ResponseEntity.badRequest().body(new CustomMessage("Product not found"));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new CustomMessage("You don't have permission."));

    }
}
