import { useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";
import "./SellerDashboard.css";

function SellerDashboard() {

    const navigate = useNavigate();

    // =====================================================
    // CURRENT LOGGED-IN SELLER
    // =====================================================

    const sellerEmail =
        localStorage.getItem("email") ||
        "seller@delulucart.com";

    const storedSellerName =
        localStorage.getItem("sellerName");

    const sellerName =
        storedSellerName ||
        sellerEmail
            .split("@")[0]
            .replace(/[._-]/g, " ")
            .replace(/\b\w/g, (char) =>
                char.toUpperCase()
            );

    const sellerId =
        localStorage.getItem("sellerId") ||
        sellerEmail;


    // =====================================================
    // STATES
    // =====================================================

    const [activeMenu, setActiveMenu] =
        useState("dashboard");

    const [search, setSearch] =
        useState("");

    const [selectedCategory, setSelectedCategory] =
        useState("All");

    const [showProductModal, setShowProductModal] =
        useState(false);

    const [editingProduct, setEditingProduct] =
        useState(null);

    const [viewProduct, setViewProduct] =
        useState(null);

    const [cart, setCart] =
        useState([]);

    const [wishlist, setWishlist] =
        useState([]);

    const [orders, setOrders] =
        useState([]);

    const [toast, setToast] =
        useState("");


    // =====================================================
    // PRODUCT FORM
    // =====================================================

    const [productForm, setProductForm] =
        useState({
            name: "",
            category: "Fashion",
            price: "",
            stock: "",
            image: "",
            description: ""
        });


    // =====================================================
    // DEMO PRODUCTS
    // sellerId identifies product owner
    // =====================================================

    const [products, setProducts] =
        useState([

            {
                id: 1,
                name: "Premium Sneakers",
                category: "Fashion",
                price: 89.99,
                stock: 20,
                image:
                    "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=800",
                description:
                    "Premium stylish sneakers designed for everyday comfort.",
                sellerId: "other-seller-1",
                sellerName: "Arun Fashion"
            },

            {
                id: 2,
                name: "Wireless Headphones",
                category: "Electronics",
                price: 129.99,
                stock: 15,
                image:
                    "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=800",
                description:
                    "High quality wireless headphones with immersive sound.",
                sellerId: "other-seller-2",
                sellerName: "Tech World"
            },

            {
                id: 3,
                name: "Smart Watch",
                category: "Electronics",
                price: 199.99,
                stock: 10,
                image:
                    "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=800",
                description:
                    "Smart watch with fitness tracking and modern design.",
                sellerId: "other-seller-3",
                sellerName: "Smart Store"
            },

            {
                id: 4,
                name: "Leather Handbag",
                category: "Fashion",
                price: 79.99,
                stock: 12,
                image:
                    "https://images.unsplash.com/photo-1584917865442-de89df76afd3?w=800",
                description:
                    "Elegant leather handbag for modern fashion lovers.",
                sellerId: sellerId,
                sellerName: sellerName
            },

            {
                id: 5,
                name: "Modern Chair",
                category: "Home",
                price: 149.99,
                stock: 8,
                image:
                    "https://images.unsplash.com/photo-1503602642458-232111445657?w=800",
                description:
                    "Modern comfortable chair for your beautiful home.",
                sellerId: "other-seller-4",
                sellerName: "Home Decor"
            },

            {
                id: 6,
                name: "Minimal Lamp",
                category: "Home",
                price: 49.99,
                stock: 25,
                image:
                    "https://images.unsplash.com/photo-1507473885765-e6ed057f782c?w=800",
                description:
                    "Minimal decorative lamp for a stylish interior.",
                sellerId: sellerId,
                sellerName: sellerName
            }

        ]);


    // =====================================================
    // CATEGORIES
    // =====================================================

    const categories = [
        "All",
        "Fashion",
        "Electronics",
        "Home",
        "Beauty",
        "Sports"
    ];


    // =====================================================
    // FILTER PRODUCTS
    // =====================================================

    const filteredProducts = useMemo(() => {

        return products.filter((product) => {

            const searchMatch =
                product.name
                    .toLowerCase()
                    .includes(
                        search.toLowerCase()
                    );

            const categoryMatch =
                selectedCategory === "All" ||
                product.category ===
                selectedCategory;

            return (
                searchMatch &&
                categoryMatch
            );
        });

    }, [
        products,
        search,
        selectedCategory
    ]);


    // =====================================================
    // MY PRODUCTS
    // =====================================================

    const myProducts =
        products.filter(
            (product) =>
                product.sellerId === sellerId
        );


    // =====================================================
    // SHOW TOAST
    // =====================================================

    const showToast = (message) => {

        setToast(message);

        setTimeout(() => {

            setToast("");

        }, 2500);
    };


    // =====================================================
    // FORM CHANGE
    // =====================================================

    const handleFormChange = (event) => {

        const {
            name,
            value
        } = event.target;

        setProductForm({

            ...productForm,

            [name]: value

        });
    };


    // =====================================================
    // OPEN ADD PRODUCT
    // =====================================================

    const openAddProduct = () => {

        setEditingProduct(null);

        setProductForm({

            name: "",
            category: "Fashion",
            price: "",
            stock: "",
            image: "",
            description: ""

        });

        setShowProductModal(true);
    };


    // =====================================================
    // OPEN UPDATE PRODUCT
    // =====================================================

    const openEditProduct = (product) => {

        setEditingProduct(product);

        setProductForm({

            name: product.name,
            category: product.category,
            price: product.price,
            stock: product.stock,
            image: product.image,
            description:
                product.description

        });

        setShowProductModal(true);
    };


    // =====================================================
    // ADD / UPDATE PRODUCT
    // =====================================================

    const handleProductSubmit = (event) => {

        event.preventDefault();

        if (
            !productForm.name ||
            !productForm.price ||
            !productForm.stock
        ) {

            showToast(
                "Please fill all required fields"
            );

            return;
        }


        if (editingProduct) {

            setProducts(
                products.map((product) =>

                    product.id ===
                    editingProduct.id

                        ? {
                            ...product,
                            ...productForm,
                            price:
                                Number(
                                    productForm.price
                                ),
                            stock:
                                Number(
                                    productForm.stock
                                )
                        }

                        : product
                )
            );

            showToast(
                "Product updated successfully ✨"
            );

        } else {

            const newProduct = {

                id:
                    Date.now(),

                ...productForm,

                price:
                    Number(
                        productForm.price
                    ),

                stock:
                    Number(
                        productForm.stock
                    ),

                sellerId:
                    sellerId,

                sellerName:
                    sellerName

            };

            setProducts([
                newProduct,
                ...products
            ]);

            showToast(
                "Product added successfully 🎉"
            );
        }

        setShowProductModal(false);
    };


    // =====================================================
    // DELETE PRODUCT
    // =====================================================

    const handleDeleteProduct = (productId) => {

        const confirmDelete =
            window.confirm(
                "Are you sure you want to delete this product?"
            );

        if (!confirmDelete) {
            return;
        }

        setProducts(

            products.filter(
                (product) =>
                    product.id !== productId
            )

        );

        showToast(
            "Product deleted successfully 🗑️"
        );
    };


    // =====================================================
    // ADD TO CART
    // =====================================================

    const addToCart = (product) => {

        if (
            product.sellerId === sellerId
        ) {

            showToast(
                "You cannot buy your own product"
            );

            return;
        }


        const alreadyInCart =
            cart.some(
                (item) =>
                    item.id === product.id
            );


        if (alreadyInCart) {

            showToast(
                "Product is already in your cart 🛒"
            );

            return;
        }


        setCart([
            ...cart,
            product
        ]);

        showToast(
            "Added to cart 🛒"
        );
    };


    // =====================================================
    // BUY NOW
    // =====================================================

    const buyNow = (product) => {

        if (
            product.sellerId === sellerId
        ) {

            showToast(
                "You cannot buy your own product"
            );

            return;
        }


        const newOrder = {

            id:
                Date.now(),

            product:
                product,

            quantity:
                1,

            total:
                product.price,

            date:
                new Date()
                    .toLocaleDateString(
                        "en-US"
                    ),

            status:
                "Confirmed"

        };


        setOrders([
            newOrder,
            ...orders
        ]);


        showToast(
            "Order placed successfully 🎉"
        );
    };


    // =====================================================
    // WISHLIST
    // =====================================================

    const toggleWishlist = (product) => {

        const exists =
            wishlist.some(
                (item) =>
                    item.id === product.id
            );


        if (exists) {

            setWishlist(

                wishlist.filter(
                    (item) =>
                        item.id !==
                        product.id
                )

            );

            showToast(
                "Removed from wishlist"
            );

        } else {

            setWishlist([
                ...wishlist,
                product
            ]);

            showToast(
                "Added to wishlist ❤️"
            );
        }
    };


    // =====================================================
    // LOGOUT
    // =====================================================

    const handleLogout = () => {

        localStorage.removeItem(
            "token"
        );

        localStorage.removeItem(
            "role"
        );

        localStorage.removeItem(
            "email"
        );

        localStorage.removeItem(
            "sellerName"
        );

        navigate("/login");
    };


    // =====================================================
    // DASHBOARD STATS
    // =====================================================

    const totalProducts =
        myProducts.length;

    const totalStock =
        myProducts.reduce(
            (total, product) =>
                total +
                Number(product.stock),
            0
        );

    const totalSales =
        orders.reduce(
            (total, order) =>
                total +
                Number(order.total),
            0
        );


    // =====================================================
    // RENDER
    // =====================================================

    return (

        <div className="seller-dashboard">


            {/* =================================================
                SIDEBAR
            ================================================= */}

            <aside className="seller-sidebar">

                <div className="sidebar-brand">

                    <div className="brand-icon">
                        🛍️
                    </div>

                    <div>

                        <h2>
                            DeluLu
                        </h2>

                        <span>
                            Cart
                        </span>

                    </div>

                </div>


                {/* PROFILE */}

                <div className="seller-profile">

                    <div className="profile-avatar">

                        {sellerName
                            .charAt(0)
                            .toUpperCase()
                        }

                    </div>

                    <div>

                        <h3>
                            {sellerName}
                        </h3>

                        <p>
                            Seller Account
                        </p>

                    </div>

                </div>


                {/* MENU */}

                <nav className="sidebar-menu">

                    <button
                        className={
                            activeMenu ===
                            "dashboard"
                                ? "active"
                                : ""
                        }
                        onClick={() =>
                            setActiveMenu(
                                "dashboard"
                            )
                        }
                    >
                        📊
                        Dashboard
                    </button>


                    <button
                        className={
                            activeMenu ===
                            "products"
                                ? "active"
                                : ""
                        }
                        onClick={() =>
                            setActiveMenu(
                                "products"
                            )
                        }
                    >
                        📦
                        My Products
                    </button>


                    <button
                        className={
                            activeMenu ===
                            "shop"
                                ? "active"
                                : ""
                        }
                        onClick={() =>
                            setActiveMenu(
                                "shop"
                            )
                        }
                    >
                        🛍️
                        Shop Products
                    </button>


                    <button
                        className={
                            activeMenu ===
                            "orders"
                                ? "active"
                                : ""
                        }
                        onClick={() =>
                            setActiveMenu(
                                "orders"
                            )
                        }
                    >
                        🚚
                        My Orders
                    </button>


                    <button
                        className={
                            activeMenu ===
                            "wishlist"
                                ? "active"
                                : ""
                        }
                        onClick={() =>
                            setActiveMenu(
                                "wishlist"
                            )
                        }
                    >
                        ❤️
                        Wishlist
                    </button>


                    <button
                        className={
                            activeMenu ===
                            "cart"
                                ? "active"
                                : ""
                        }
                        onClick={() =>
                            setActiveMenu(
                                "cart"
                            )
                        }
                    >
                        🛒
                        Cart

                        {cart.length >
                            0 && (

                            <span className="menu-count">

                                {cart.length}

                            </span>

                        )}

                    </button>

                </nav>


                <button
                    className="sidebar-logout"
                    onClick={
                        handleLogout
                    }
                >

                    🚪
                    Logout

                </button>

            </aside>


            {/* =================================================
                MAIN CONTENT
            ================================================= */}

            <main className="seller-main">


                {/* TOP BAR */}

                <header className="seller-topbar">

                    <div>

                        <p>
                            Welcome back,
                        </p>

                        <h1>
                            {sellerName} 👋
                        </h1>

                    </div>


                    <div className="top-actions">

                        <button
                            className="top-cart"
                            onClick={() =>
                                setActiveMenu(
                                    "cart"
                                )
                            }
                        >

                            🛒

                            {cart.length >
                                0 && (

                                <span>
                                    {cart.length}
                                </span>

                            )}

                        </button>


                        <div className="top-avatar">

                            {sellerName
                                .charAt(0)
                                .toUpperCase()
                            }

                        </div>

                    </div>

                </header>


                {/* =================================================
                    DASHBOARD
                ================================================= */}

                {activeMenu ===
                    "dashboard" && (

                    <>

                        <section className="hero-banner">

                            <div>

                                <span>
                                    ✨ Seller Hub
                                </span>

                                <h2>
                                    Grow your business
                                    with DeluLu Cart
                                </h2>

                                <p>
                                    Manage products,
                                    explore the marketplace,
                                    and build your brand.
                                </p>

                                <button
                                    onClick={
                                        openAddProduct
                                    }
                                >
                                    + Add New Product
                                </button>

                            </div>

                            <div className="hero-art">

                                🛍️
                                📦
                                ✨

                            </div>

                        </section>


                        {/* STATS */}

                        <section className="stats-grid">

                            <div className="stat-card">

                                <span>
                                    📦
                                </span>

                                <div>

                                    <p>
                                        My Products
                                    </p>

                                    <h2>
                                        {totalProducts}
                                    </h2>

                                </div>

                            </div>


                            <div className="stat-card">

                                <span>
                                    📊
                                </span>

                                <div>

                                    <p>
                                        Available Stock
                                    </p>

                                    <h2>
                                        {totalStock}
                                    </h2>

                                </div>

                            </div>


                            <div className="stat-card">

                                <span>
                                    🚚
                                </span>

                                <div>

                                    <p>
                                        My Orders
                                    </p>

                                    <h2>
                                        {orders.length}
                                    </h2>

                                </div>

                            </div>


                            <div className="stat-card">

                                <span>
                                    💰
                                </span>

                                <div>

                                    <p>
                                        Shopping Total
                                    </p>

                                    <h2>
                                        $
                                        {totalSales.toFixed(
                                            2
                                        )}
                                    </h2>

                                </div>

                            </div>

                        </section>

                    </>
                )}


                {/* =================================================
                    MY PRODUCTS
                ================================================= */}

                {activeMenu ===
                    "products" && (

                    <ProductSection
                        title="My Products"
                        products={
                            myProducts
                        }
                        sellerId={
                            sellerId
                        }
                        onAdd={
                            openAddProduct
                        }
                        onEdit={
                            openEditProduct
                        }
                        onDelete={
                            handleDeleteProduct
                        }
                        onView={
                            setViewProduct
                        }
                        onWishlist={
                            toggleWishlist
                        }
                        wishlist={
                            wishlist
                        }
                        onCart={
                            addToCart
                        }
                        onBuy={
                            buyNow
                        }
                        search={
                            search
                        }
                        setSearch={
                            setSearch
                        }
                        selectedCategory={
                            selectedCategory
                        }
                        setSelectedCategory={
                            setSelectedCategory
                        }
                        categories={
                            categories
                        }
                    />

                )}


                {/* =================================================
                    SHOP
                ================================================= */}

                {activeMenu ===
                    "shop" && (

                    <ProductSection
                        title="Explore Products"
                        products={
                            filteredProducts
                        }
                        sellerId={
                            sellerId
                        }
                        onAdd={
                            openAddProduct
                        }
                        onEdit={
                            openEditProduct
                        }
                        onDelete={
                            handleDeleteProduct
                        }
                        onView={
                            setViewProduct
                        }
                        onWishlist={
                            toggleWishlist
                        }
                        wishlist={
                            wishlist
                        }
                        onCart={
                            addToCart
                        }
                        onBuy={
                            buyNow
                        }
                        search={
                            search
                        }
                        setSearch={
                            setSearch
                        }
                        selectedCategory={
                            selectedCategory
                        }
                        setSelectedCategory={
                            setSelectedCategory
                        }
                        categories={
                            categories
                        }
                    />

                )}


                {/* =================================================
                    ORDERS
                ================================================= */}

                {activeMenu ===
                    "orders" && (

                    <section className="content-section">

                        <div className="section-title">

                            <div>

                                <span>
                                    Shopping
                                </span>

                                <h2>
                                    My Orders
                                </h2>

                            </div>

                        </div>


                        {orders.length ===
                            0 ? (

                            <div className="empty-state">

                                <div>
                                    📦
                                </div>

                                <h3>
                                    No orders yet
                                </h3>

                                <p>
                                    Start shopping
                                    products from
                                    other sellers.
                                </p>

                            </div>

                        ) : (

                            <div className="orders-list">

                                {orders.map(
                                    (order) => (

                                    <div
                                        className="order-card"
                                        key={
                                            order.id
                                        }
                                    >

                                        <img
                                            src={
                                                order
                                                    .product
                                                    .image
                                            }
                                            alt={
                                                order
                                                    .product
                                                    .name
                                            }
                                        />

                                        <div>

                                            <h3>
                                                {
                                                    order
                                                        .product
                                                        .name
                                                }
                                            </h3>

                                            <p>
                                                Seller:
                                                {" "}
                                                {
                                                    order
                                                        .product
                                                        .sellerName
                                                }
                                            </p>

                                            <small>
                                                Ordered on:
                                                {" "}
                                                {
                                                    order.date
                                                }
                                            </small>

                                        </div>

                                        <strong>
                                            $
                                            {order.total.toFixed(
                                                2
                                            )}
                                        </strong>

                                        <span className="order-status">
                                            {order.status}
                                        </span>

                                    </div>

                                ))}

                            </div>

                        )}

                    </section>

                )}


                {/* =================================================
                    WISHLIST
                ================================================= */}

                {activeMenu ===
                    "wishlist" && (

                    <section className="content-section">

                        <div className="section-title">

                            <div>

                                <span>
                                    Your Favorites
                                </span>

                                <h2>
                                    Wishlist ❤️
                                </h2>

                            </div>

                        </div>


                        {wishlist.length ===
                            0 ? (

                            <div className="empty-state">

                                <div>
                                    ❤️
                                </div>

                                <h3>
                                    Your wishlist is empty
                                </h3>

                                <p>
                                    Save products you love.
                                </p>

                            </div>

                        ) : (

                            <div className="product-grid">

                                {wishlist.map(
                                    (product) => (

                                    <ProductCard
                                        key={
                                            product.id
                                        }
                                        product={
                                            product
                                        }
                                        sellerId={
                                            sellerId
                                        }
                                        onView={
                                            setViewProduct
                                        }
                                        onWishlist={
                                            toggleWishlist
                                        }
                                        wishlist={
                                            wishlist
                                        }
                                        onCart={
                                            addToCart
                                        }
                                        onBuy={
                                            buyNow
                                        }
                                        onEdit={
                                            openEditProduct
                                        }
                                        onDelete={
                                            handleDeleteProduct
                                        }
                                    />

                                ))}

                            </div>

                        )}

                    </section>

                )}


                {/* =================================================
                    CART
                ================================================= */}

                {activeMenu ===
                    "cart" && (

                    <section className="content-section">

                        <div className="section-title">

                            <div>

                                <span>
                                    Ready to checkout
                                </span>

                                <h2>
                                    My Cart 🛒
                                </h2>

                            </div>

                        </div>


                        {cart.length ===
                            0 ? (

                            <div className="empty-state">

                                <div>
                                    🛒
                                </div>

                                <h3>
                                    Your cart is empty
                                </h3>

                                <p>
                                    Explore products
                                    from other sellers.
                                </p>

                            </div>

                        ) : (

                            <div className="cart-list">

                                {cart.map(
                                    (product) => (

                                    <div
                                        className="cart-item"
                                        key={
                                            product.id
                                        }
                                    >

                                        <img
                                            src={
                                                product.image
                                            }
                                            alt={
                                                product.name
                                            }
                                        />

                                        <div>

                                            <h3>
                                                {
                                                    product.name
                                                }
                                            </h3>

                                            <p>
                                                Seller:
                                                {" "}
                                                {
                                                    product.sellerName
                                                }
                                            </p>

                                            <strong>
                                                $
                                                {product.price.toFixed(
                                                    2
                                                )}
                                            </strong>

                                        </div>

                                        <button
                                            onClick={() =>
                                                buyNow(
                                                    product
                                                )
                                            }
                                        >
                                            Buy Now
                                        </button>

                                    </div>

                                ))}

                            </div>

                        )}

                    </section>

                )}

            </main>


            {/* =================================================
                PRODUCT MODAL
            ================================================= */}

            {showProductModal && (

                <div className="modal-overlay">

                    <div className="product-modal">

                        <button
                            className="modal-close"
                            onClick={() =>
                                setShowProductModal(
                                    false
                                )
                            }
                        >
                            ×
                        </button>

                        <h2>
                            {editingProduct
                                ? "Update Product"
                                : "Add New Product"
                            }
                        </h2>

                        <p>
                            Add your product details
                            to DeluLu Cart.
                        </p>


                        <form
                            onSubmit={
                                handleProductSubmit
                            }
                        >

                            <input
                                name="name"
                                placeholder="Product Name"
                                value={
                                    productForm.name
                                }
                                onChange={
                                    handleFormChange
                                }
                                required
                            />


                            <select
                                name="category"
                                value={
                                    productForm.category
                                }
                                onChange={
                                    handleFormChange
                                }
                            >

                            {categories
    .filter(
        (cat) => cat !== "All"
    )
    .map(
        (cat) => (
            <option
                key={cat}
                value={cat}
            >
                {cat}
            </option>
        )
    )}

                            </select>


                            <input
                                type="number"
                                name="price"
                                placeholder="Price"
                                value={
                                    productForm.price
                                }
                                onChange={
                                    handleFormChange
                                }
                                min="0"
                                step="0.01"
                                required
                            />


                            <input
                                type="number"
                                name="stock"
                                placeholder="Stock Quantity"
                                value={
                                    productForm.stock
                                }
                                onChange={
                                    handleFormChange
                                }
                                min="0"
                                required
                            />


                            <input
                                type="url"
                                name="image"
                                placeholder="Product Image URL"
                                value={
                                    productForm.image
                                }
                                onChange={
                                    handleFormChange
                                }
                            />


                            <textarea
                                name="description"
                                placeholder="Product Description"
                                value={
                                    productForm.description
                                }
                                onChange={
                                    handleFormChange
                                }
                            />


                            <button
                                type="submit"
                            >
                                {editingProduct
                                    ? "Update Product"
                                    : "Add Product"
                                }
                            </button>

                        </form>

                    </div>

                </div>

            )}


            {/* =================================================
                VIEW PRODUCT MODAL
            ================================================= */}

            {viewProduct && (

                <div className="modal-overlay">

                    <div className="view-modal">

                        <button
                            className="modal-close"
                            onClick={() =>
                                setViewProduct(
                                    null
                                )
                            }
                        >
                            ×
                        </button>

                        <img
                            src={
                                viewProduct.image
                            }
                            alt={
                                viewProduct.name
                            }
                        />

                        <div>

                            <span>
                                {
                                    viewProduct.category
                                }
                            </span>

                            <h2>
                                {
                                    viewProduct.name
                                }
                            </h2>

                            <p>
                                {
                                    viewProduct.description
                                }
                            </p>

                            <h3>
                                $
                                {
                                    viewProduct.price.toFixed(
                                        2
                                    )
                                }
                            </h3>

                            <p>
                                Seller:
                                {" "}
                                {
                                    viewProduct.sellerName
                                }
                            </p>


                            {viewProduct.sellerId !==
                                sellerId && (

                                <div className="view-actions">

                                    <button
                                        onClick={() =>
                                            addToCart(
                                                viewProduct
                                            )
                                        }
                                    >
                                        🛒 Add to Cart
                                    </button>

                                    <button
                                        onClick={() =>
                                            buyNow(
                                                viewProduct
                                            )
                                        }
                                    >
                                        ⚡ Buy Now
                                    </button>

                                </div>

                            )}

                        </div>

                    </div>

                </div>

            )}


            {/* =================================================
                TOAST
            ================================================= */}

            {toast && (

                <div className="toast-message">

                    {toast}

                </div>

            )}

        </div>

    );
}


// =============================================================
// PRODUCT SECTION COMPONENT
// =============================================================

function ProductSection({

    title,
    products,
    sellerId,
    onAdd,
    onEdit,
    onDelete,
    onView,
    onWishlist,
    wishlist,
    onCart,
    onBuy,
    search,
    setSearch,
    selectedCategory,
    setSelectedCategory,
    categories

}) {

    return (

        <section className="content-section">

            <div className="section-title">

                <div>

                    <span>
                        DeluLu Marketplace
                    </span>

                    <h2>
                        {title}
                    </h2>

                </div>

                <button
                    className="add-product-button"
                    onClick={onAdd}
                >
                    + Add Product
                </button>

            </div>


            {/* SEARCH FILTER */}

            <div className="product-toolbar">

                <div className="dashboard-search">

                    🔍

                    <input
                        placeholder="Search products..."
                        value={search}
                        onChange={(event) =>
                            setSearch(
                                event.target.value
                            )
                        }
                    />

                </div>


                <div className="category-filters">

                    {categories.map(
                        (category) => (

                        <button
                            key={
                                category
                            }
                            className={
                                selectedCategory ===
                                category
                                    ? "active"
                                    : ""
                            }
                            onClick={() =>
                                setSelectedCategory(
                                    category
                                )
                            }
                        >

                            {category}

                        </button>

                    ))}

                </div>

            </div>


              <div className="product-grid">

    {filteredProducts.length > 0 ? (

        filteredProducts.map((product) => (

            <div
                className="product-card"
                key={product.id}
            >

                <img
                    src={product.image}
                    alt={product.name}
                />

                <h3>
                    {product.name}
                </h3>

                <p>
                    ${product.price}
                </p>

            </div>

        ))

    ) : (

        <div className="empty-state">

            <div>
                🔍
            </div>

            <h3>
                No products found
            </h3>

            <p>
                Try another search or category.
            </p>

        </div>

    )}

</div>

            </section>

    );
}


// =============================================================
// PRODUCT CARD COMPONENT
// =============================================================

function ProductCard({

    product,
    sellerId,
    onView,
    onWishlist,
    wishlist,
    onCart,
    onBuy,
    onEdit,
    onDelete

}) {

    const isOwnProduct =
        product.sellerId ===
        sellerId;

    const isWishlisted =
        wishlist.some(
            (item) =>
                item.id === product.id
        );


    return (

        <article className="product-card">

            <div className="product-image-wrapper">

                <img
                    src={
                        product.image ||
                        "https://via.placeholder.com/600x500?text=DeluLu+Cart"
                    }
                    alt={
                        product.name
                    }
                />


                <button
                    className={
                        isWishlisted
                            ? "wishlist active"
                            : "wishlist"
                    }
                    onClick={() =>
                        onWishlist(
                            product
                        )
                    }
                >
                    {isWishlisted
                        ? "♥"
                        : "♡"
                    }
                </button>


                {isOwnProduct && (

                    <span className="own-product-badge">
                        Your Product
                    </span>

                )}

            </div>


            <div className="product-info">

                <span className="product-category">
                    {
                        product.category
                    }
                </span>

                <h3>
                    {
                        product.name
                    }
                </h3>

                <p className="seller-name">
                    Sold by:
                    {" "}
                    <strong>
                        {
                            product.sellerName
                        }
                    </strong>
                </p>


                <div className="product-price-row">

                    <strong>
                        $
                        {
                            Number(
                                product.price
                            ).toFixed(2)
                        }
                    </strong>

                    <span>
                        Stock:
                        {" "}
                        {
                            product.stock
                        }
                    </span>

                </div>


                <div className="product-actions">

                    <button
                        className="view-button"
                        onClick={() =>
                            onView(
                                product
                            )
                        }
                    >
                        👁 View
                    </button>


                    {isOwnProduct ? (

                        <>

                            <button
                                className="edit-button"
                                onClick={() =>
                                    onEdit(
                                        product
                                    )
                                }
                            >
                                ✏️ Edit
                            </button>

                            <button
                                className="delete-button"
                                onClick={() =>
                                    onDelete(
                                        product.id
                                    )
                                }
                            >
                                🗑️
                            </button>

                        </>

                    ) : (

                        <>

                            <button
                                className="cart-action"
                                onClick={() =>
                                    onCart(
                                        product
                                    )
                                }
                            >
                                🛒
                            </button>

                            <button
                                className="buy-action"
                                onClick={() =>
                                    onBuy(
                                        product
                                    )
                                }
                            >
                                Buy Now
                            </button>

                        </>

                    )}

                </div>

            </div>

        </article>

    );
}


export default SellerDashboard;