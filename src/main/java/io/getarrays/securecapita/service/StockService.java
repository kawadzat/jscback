package io.getarrays.securecapita.service;

import io.getarrays.securecapita.domain.Stock;

import io.getarrays.securecapita.repository.StockRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

public class StockService {
    @Autowired
    private StockRepository  stockRepository;
    /* to create user */
    public Stock createStock(Stock stock) {
        Stock createdStock = (Stock) stockRepository.save(stock);
        return createdStock;
    }
    public boolean findProductByCode(String productCode) {
        Stock stock = stockRepository.findByProductCode(productCode);
        return stock != null;
    }
    public int getTotalQuantityByProductCode(String productCode) {
        Stock product = stockRepository.findByProductCode(productCode);
        if (product == null) {
            throw new EntityNotFoundException("Product not found with product code: " + productCode);
        }
        return product.getQuantity();
    }




//    public boolean isProductInStock(Long productId) {
//        Optional<Stock> optionalProduct = stockRepository.findById(productId);
//        if (optionalProduct.isPresent()) {
//            Stock product = optionalProduct.get();
//            if (product.getQuantity() > 0) {
//                return true;
//            }
//        }
//        return false;
//    }




    public Stock getProductBasedOnId(  Long stockId) {
        return stockRepository.findById(stockId).get();
    }
//
//
////    public Product updateProduct(Long productId, Product updatedProduct) {
////        Product existingProduct = productRepository.findById(updatedProduct.getId())
////                .orElseThrow(() -> new RuntimeException("Product not found"));
////
////    }
//
//
//    public Product updateProduct(Product productObject)
//    {
//        return productRepository.save(productObject);
//    }
//
//
//
//
public boolean deleteStock(Long productId) {
    try {
        stockRepository.deleteById(productId);
        return true;  // Deletion successful
    } catch (Exception e) {
        return false; // Failed to delete stock
    }
}









  public List<Stock> getAllStock() {
      return  stockRepository.findAll();
    }

    public boolean isProductInStock(String productCode) {
        Stock stock = stockRepository.findByProductCode(productCode);
        return stock != null;
    }



}
