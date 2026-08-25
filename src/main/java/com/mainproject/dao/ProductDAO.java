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

                System.out.println(
                        "Product is null."
                );

                return false;
            }

            /*
             * Make sure farmerEmail is already present
             * in Product before saving.
             */
            if (product.getFarmerEmail() == null
                    || product.getFarmerEmail().trim().isEmpty()) {

                System.out.println(
                        "Farmer email is missing."
                );

                return false;
            }

            DocumentReference document =
                    db.collection("products")
                            .document();

            // Generate Firestore document ID
            String productId =
                    document.getId();

            product.setProductId(
                    productId
            );

            // Save complete Product object
            document.set(product)
                    .get();

            System.out.println(
                    "===================================="
            );

            System.out.println(
                    "Product saved successfully!"
            );

            System.out.println(
                    "Product ID: "
                            + productId
            );

            System.out.println(
                    "Product Name: "
                            + product.getName()
            );

            System.out.println(
                    "Farmer Email: "
                            + product.getFarmerEmail()
            );

            System.out.println(
                    "Status: "
                            + product.getStatus()
            );

            System.out.println(
                    "===================================="
            );

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error saving product:"
            );

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // ADD PRODUCT WITH FARMER EMAIL
    // =====================================================
    /*
     * This method is used by AddProduct.java
     *
     * It guarantees that the farmerEmail is stored
     * inside the Firestore product document.
     */

    public boolean addProduct(
            Product product,
            String farmerEmail) {

        try {

            if (product == null) {

                System.out.println(
                        "Product is null."
                );

                return false;
            }

            if (farmerEmail == null
                    || farmerEmail.trim().isEmpty()) {

                System.out.println(
                        "Farmer email is missing."
                );

                return false;
            }

            // IMPORTANT
            product.setFarmerEmail(
                    farmerEmail.trim()
            );

            return addProduct(product);

        } catch (Exception e) {

            System.out.println(
                    "Error adding product with farmer email:"
            );

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // GET SINGLE PRODUCT
    // =====================================================

    public Product getProductById(
            String productId) {

        try {

            if (productId == null
                    || productId.trim().isEmpty()) {

                return null;
            }

            DocumentSnapshot document =
                    db.collection("products")
                            .document(productId)
                            .get()
                            .get();

            if (!document.exists()) {

                System.out.println(
                        "Product not found: "
                                + productId
                );

                return null;
            }

            Product product =
                    document.toObject(
                            Product.class
                    );

            if (product != null) {

                if (product.getProductId() == null
                        || product.getProductId().isEmpty()) {

                    product.setProductId(
                            document.getId()
                    );
                }
            }

            return product;

        } catch (Exception e) {

            System.out.println(
                    "Error getting product:"
            );

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

            if (farmerEmail == null
                    || farmerEmail.trim().isEmpty()) {

                return products;
            }

            String email =
                    farmerEmail.trim();

            QuerySnapshot snapshot =
                    db.collection("products")
                            .whereEqualTo(
                                    "farmerEmail",
                                    email
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

                    if (product.getProductId() == null
                            || product.getProductId().isEmpty()) {

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

            System.out.println(
                    "Error loading farmer products:"
            );

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

                    if (product.getProductId() == null
                            || product.getProductId().isEmpty()) {

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
                    "All products loaded: "
                            + products.size()
            );

        } catch (Exception e) {

            System.out.println(
                    "Error loading all products:"
            );

            e.printStackTrace();
        }

        return products;
    }

    // =====================================================
    // GET PRODUCT COUNT BY OWNER
    // =====================================================

    public int getProductCountByOwner(
            String farmerEmail) {

        try {

            if (farmerEmail == null
                    || farmerEmail.trim().isEmpty()) {

                System.out.println(
                        "Farmer email is empty."
                );

                return 0;
            }

            String email =
                    farmerEmail.trim();

            QuerySnapshot snapshot =
                    db.collection("products")
                            .whereEqualTo(
                                    "farmerEmail",
                                    email
                            )
                            .get()
                            .get();

            int count =
                    snapshot.getDocuments().size();

            System.out.println(
                    "===================================="
            );

            System.out.println(
                    "Product Count"
            );

            System.out.println(
                    "Farmer Email: "
                            + email
            );

            System.out.println(
                    "Total Products: "
                            + count
            );

            System.out.println(
                    "===================================="
            );

            return count;

        } catch (Exception e) {

            System.out.println(
                    "Error getting product count by owner:"
            );

            e.printStackTrace();

            return 0;
        }
    }

    // =====================================================
    // GET ACTIVE PRODUCT COUNT BY OWNER
    // =====================================================

    public int getActiveProductCountByOwner(
            String farmerEmail) {

        try {

            if (farmerEmail == null
                    || farmerEmail.trim().isEmpty()) {

                System.out.println(
                        "Farmer email is empty."
                );

                return 0;
            }

            String email =
                    farmerEmail.trim();

            QuerySnapshot snapshot =
                    db.collection("products")
                            .whereEqualTo(
                                    "farmerEmail",
                                    email
                            )
                            .whereEqualTo(
                                    "status",
                                    "Active"
                            )
                            .get()
                            .get();

            int count =
                    snapshot.getDocuments().size();

            System.out.println(
                    "===================================="
            );

            System.out.println(
                    "Active Product Count"
            );

            System.out.println(
                    "Farmer Email: "
                            + email
            );

            System.out.println(
                    "Active Products: "
                            + count
            );

            System.out.println(
                    "===================================="
            );

            return count;

        } catch (Exception e) {

            System.out.println(
                    "Error getting active product count:"
            );

            e.printStackTrace();

            return 0;
        }
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

            if (productId == null
                    || productId.trim().isEmpty()) {

                System.out.println(
                        "Product ID is missing."
                );

                return false;
            }

            db.collection("products")
                    .document(productId)
                    .set(product)
                    .get();

            System.out.println(
                    "Product updated successfully!"
            );

            System.out.println(
                    "Product ID: "
                            + productId
            );

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error updating product:"
            );

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // UPDATE ONLY IMAGE URL
    // =====================================================

    public boolean updateProductImage(
            String productId,
            String imageUrl) {

        try {

            if (productId == null
                    || productId.trim().isEmpty()) {

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

            System.out.println(
                    "Error updating product image:"
            );

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // UPDATE PRODUCT STATUS
    // =====================================================

    public boolean updateProductStatus(
            String productId,
            String status) {

        try {

            if (productId == null
                    || productId.trim().isEmpty()) {

                return false;
            }

            db.collection("products")
                    .document(productId)
                    .update(
                            "status",
                            status
                    )
                    .get();

            System.out.println(
                    "Product status updated: "
                            + status
            );

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error updating product status:"
            );

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // UPDATE STOCK
    // =====================================================

    public boolean updateStock(
            String productId,
            double stock) {

        try {

            if (productId == null
                    || productId.trim().isEmpty()) {

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

            System.out.println(
                    "Error updating stock:"
            );

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // DELETE PRODUCT
    // =====================================================

    public boolean deleteProduct(
            String productId) {

        try {

            if (productId == null
                    || productId.trim().isEmpty()) {

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

            System.out.println(
                    "Error deleting product:"
            );

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

            if (productId == null
                    || productId.trim().isEmpty()) {

                return false;
            }

            DocumentSnapshot document =
                    db.collection("products")
                            .document(productId)
                            .get()
                            .get();

            return document.exists();

        } catch (Exception e) {

            System.out.println(
                    "Error checking product:"
            );

            e.printStackTrace();

            return false;
        }
    }
}