package com.mainproject.dao;

import java.util.ArrayList;
import java.util.List;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.mainproject.config.FirebaseConfig;
import com.mainproject.model.Product;

public class ProductDAO {

    private final Firestore db;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public ProductDAO() {
        db = FirebaseConfig.getFirestore();
    }

    // =====================================================
    // ADD PRODUCT
    // =====================================================

    public boolean addProduct(Product product) {

        try {

            if (product == null) {
                System.out.println("Product is null.");
                return false;
            }

            DocumentReference document =
                    db.collection("products")
                      .document();

            // Generate Firestore document ID
            String productId =document.getId();

            product.setProductId(productId);

            // Save complete Product object
            document.set(product).get();

            System.out.println(
                    "Product saved successfully!"
            );

            System.out.println("Product ID: "+ productId);

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // GET SINGLE PRODUCT
    // =====================================================

    public Product getProductById(String productId) {

        try {
            if (productId == null||productId.trim().isEmpty()) {
                return null;
            }
            DocumentSnapshot document =
                    db.collection("products")
                      .document(productId)
                      .get()
                      .get();

            if (!document.exists()) {
                System.out.println("Product not found: "+ productId);
                return null;
            }

            Product product =document.toObject(Product.class);
            return product;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // =====================================================
    // GET ALL PRODUCTS OF FARMER
    // =====================================================

    public List<Product> getFarmerProducts(
            String farmerEmail) {

        List<Product> products =
                new ArrayList<>();

        try {

            if (
                    farmerEmail == null
                            ||
                    farmerEmail.trim().isEmpty()
            ) {

                return products;
            }

            QuerySnapshot snapshot =
                    db.collection("products")
                      .whereEqualTo(
                              "farmerEmail",
                              farmerEmail
                      )
                      .get()
                      .get();

            for (
                    DocumentSnapshot document
                    : snapshot.getDocuments()
            ) {

                Product product =
                        document.toObject(
                                Product.class
                        );

                if (product != null) {

                    /*
                     * Safety:
                     * If productId was not stored inside
                     * the document, get it from Firestore.
                     */

                    if (
                            product.getProductId()
                                    == null
                                    ||
                            product.getProductId()
                                    .isEmpty()
                    ) {

                        product.setProductId(
                                document.getId()
                        );
                    }

                    products.add(
                            product
                    );
                }
            }

            System.out.println(
                    "Farmer products loaded: "
                            + products.size()
            );

        } catch (Exception e) {

            e.printStackTrace();
        }

        return products;
    }

    // =====================================================
    // GET ALL PRODUCTS
    // =====================================================

    public List<Product> getAllProducts() {

        List<Product> products =
                new ArrayList<>();

        try {

            QuerySnapshot snapshot =
                    db.collection("products")
                      .get()
                      .get();

            for (
                    DocumentSnapshot document
                    : snapshot.getDocuments()
            ) {

                Product product =
                        document.toObject(
                                Product.class
                        );

                if (product != null) {

                    if (product.getProductId()== null||product.getProductId().isEmpty()){
                        product.setProductId(document.getId());
                    }
                    products.add(product);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }

    // =====================================================
    // UPDATE PRODUCT
    // =====================================================

    public boolean updateProduct(
            Product product) {

        try {

            if (product == null) {

                System.out.println(
                        "Product is null."
                );

                return false;
            }

            String productId =
                    product.getProductId();

            if (productId == null||productId.trim().isEmpty()) {
                System.out.println("Product ID is missing.");
                return false;
            }

            db.collection("products")
              .document(productId)
              .set(product)
              .get();

            System.out.println("Product updated successfully!");
            System.out.println("Product ID: "+ productId);
            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }

    public boolean updateProductImage(
            String productId,
            String imageUrl) {

        try {

            if (
                    productId == null
                            ||
                    productId.trim().isEmpty()
            ) {

                return false;
            }

            db.collection("products")
              .document(productId)
              .update(
                      "imageUrl",
                      imageUrl
              )
              .get();

            System.out.println(
                    "Product image updated successfully!"
            );

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }
    public boolean updateProductStatus(
            String productId,
            String status) {

        try {

            if (
                    productId == null
                            ||
                    productId.trim().isEmpty()
            ) {

                return false;
            }

            db.collection("products")
              .document(productId)
              .update(
                      "status",
                      status
              )
              .get();
            System.out.println("Product status updated: "+ status);
            return true;
        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }
    public boolean updateStock(
            String productId,
            double stock) {

        try {

            if (
                    productId == null
                            ||
                    productId.trim().isEmpty()
            ) {

                return false;
            }

            db.collection("products")
              .document(productId)
              .update(
                      "stock",
                      stock
              )
              .get();

            System.out.println(
                    "Product stock updated: "
                            + stock
            );

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }

    public boolean deleteProduct(
            String productId) {

        try {

            if (
                    productId == null
                            ||
                    productId.trim().isEmpty()
            ) {

                return false;
            }

            db.collection("products")
              .document(productId)
              .delete()
              .get();

            System.out.println(
                    "Product deleted successfully!"
            );

            System.out.println(
                    "Product ID: "
                            + productId
            );

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // CHECK PRODUCT EXISTS
    // =====================================================

    public boolean productExists(
            String productId) {

        try {

            if (
                    productId == null
                            ||
                    productId.trim().isEmpty()
            ) {

                return false;
            }

            DocumentSnapshot document =
                    db.collection("products")
                      .document(productId)
                      .get()
                      .get();

            return document.exists();

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }
}