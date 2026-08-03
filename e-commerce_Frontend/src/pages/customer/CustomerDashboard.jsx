import { useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";
import "./CustomerDashboard.css";

function CustomerDashboard() {

    const navigate = useNavigate();

    // =====================================================
    // CUSTOMER DETAILS
    // =====================================================

    const storedEmail =
        localStorage.getItem("email") ||
        "customer@delulucart.com";

    const storedName =
        localStorage.getItem("firstName") ||
        localStorage.getItem("name") ||
        storedEmail.split("@")[0];

    const [customerName, setCustomerName] =
        useState(
            storedName.charAt(0).toUpperCase() +
            storedName.slice(1)
        );

    const [customerEmail] =
        useState(storedEmail);


    // =====================================================
    // UI STATES
    // =====================================================

    const [activeSection, setActiveSection] =
        useState("home");

    const [search, setSearch] =
        useState("");

    const [selectedCategory, setSelectedCategory] =
        useState("All");

    const [showProfile, setShowProfile] =
        useState(false);

    const [showNotifications, setShowNotifications] =
        useState(false);

    const [showCart, setShowCart] =
        useState(false);

    const [selectedProduct, setSelectedProduct] =
        useState(null);

    const [showCheckout, setShowCheckout] =
        useState(false);

    const [showEditProfile, setShowEditProfile] =
        useState(false);


    // =====================================================
    // PRODUCTS
    // =====================================================

    const [products] = useState([

        {
            id: 1,
            name: "Premium Sneakers",
            category: "Fashion",
            price: 89.99,
            rating: 4.8,
            reviews: 124,
            image:
                "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=800",
            description:
                "Premium comfortable sneakers designed for everyday style and performance.",
            seller: "Urban Style Store"
        },

        {
            id: 2,
            name: "Wireless Headphones",
            category: "Electronics",
            price: 129.99,
            rating: 4.7,
            reviews: 89,
            image:
                "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=800",
            description:
                "Enjoy immersive sound with premium wireless headphones.",
            seller: "Tech World"
        },

        {
            id: 3,
            name: "Smart Watch Pro",
            category: "Electronics",
            price: 199.99,
            rating: 4.9,
            reviews: 212,
            image:
                "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=800",
            description:
                "Track your health, fitness and notifications with a stylish smartwatch.",
            seller: "Future Gadgets"
        },

        {
            id: 4,
            name: "Luxury Leather Handbag",
            category: "Fashion",
            price: 79.99,
            rating: 4.6,
            reviews: 76,
            image:
                "https://images.unsplash.com/photo-1584917865442-de89df76afd3?w=800",
            description:
                "Elegant leather handbag designed for modern fashion lovers.",
            seller: "Fashion Hub"
        },

        {
            id: 5,
            name: "Modern Comfort Chair",
            category: "Home",
            price: 149.99,
            rating: 4.5,
            reviews: 54,
            image:
                "https://images.unsplash.com/photo-1503602642458-232111445657?w=800",
            description:
                "A modern comfortable chair that adds style to your home.",
            seller: "Home Decor"
        },

        {
            id: 6,
            name: "Minimal Table Lamp",
            category: "Home",
            price: 49.99,
            rating: 4.7,
            reviews: 91,
            image:
                "https://images.unsplash.com/photo-1507473885765-e6ed057f782c?w=800",
            description:
                "Minimal decorative lamp perfect for modern interiors.",
            seller: "Home Decor"
        },

        {
            id: 7,
            name: "Classic Sunglasses",
            category: "Fashion",
            price: 39.99,
            rating: 4.4,
            reviews: 43,
            image:
                "https://images.unsplash.com/photo-1511499767150-a48a237f0083?w=800",
            description:
                "Classic sunglasses with a stylish modern frame.",
            seller: "Urban Style Store"
        },

        {
            id: 8,
            name: "Premium Camera",
            category: "Electronics",
            price: 599.99,
            rating: 4.9,
            reviews: 176,
            image:
                "https://images.unsplash.com/photo-1516035069371-29a1b244cc32?w=800",
            description:
                "Capture beautiful memories with this premium digital camera.",
            seller: "Tech World"
        }

    ]);


    // =====================================================
    // CATEGORIES
    // =====================================================

    const categories = [
        {
            name: "All",
            icon: "✨"
        },
        {
            name: "Fashion",
            icon: "👗"
        },
        {
            name: "Electronics",
            icon: "📱"
        },
        {
            name: "Home",
            icon: "🏠"
        }
    ];


    // =====================================================
    // CART
    // =====================================================

    const [cart, setCart] = useState(() => {

        const savedCart =
            localStorage.getItem("customerCart");

        return savedCart
            ? JSON.parse(savedCart)
            : [];

    });


    // =====================================================
    // WISHLIST
    // =====================================================

    const [wishlist, setWishlist] =
        useState(() => {

            const savedWishlist =
                localStorage.getItem(
                    "customerWishlist"
                );

            return savedWishlist
                ? JSON.parse(savedWishlist)
                : [];

        });


    // =====================================================
    // ORDERS
    // =====================================================

    const [orders, setOrders] =
        useState(() => {

            const savedOrders =
                localStorage.getItem(
                    "customerOrders"
                );

            return savedOrders
                ? JSON.parse(savedOrders)
                : [];

        });


    // =====================================================
    // SAVE CART
    // =====================================================

    useEffect(() => {

        localStorage.setItem(
            "customerCart",
            JSON.stringify(cart)
        );

    }, [cart]);


    // =====================================================
    // SAVE WISHLIST
    // =====================================================

    useEffect(() => {

        localStorage.setItem(
            "customerWishlist",
            JSON.stringify(wishlist)
        );

    }, [wishlist]);


    // =====================================================
    // SAVE ORDERS
    // =====================================================

    useEffect(() => {

        localStorage.setItem(
            "customerOrders",
            JSON.stringify(orders)
        );

    }, [orders]);


    // =====================================================
    // FILTER PRODUCTS
    // =====================================================

    const filteredProducts = useMemo(() => {

        return products.filter((product) => {

            const matchesSearch =
                product.name
                    .toLowerCase()
                    .includes(
                        search.toLowerCase()
                    );

            const matchesCategory =
                selectedCategory === "All" ||
                product.category ===
                selectedCategory;

            return (
                matchesSearch &&
                matchesCategory
            );

        });

    }, [
        products,
        search,
        selectedCategory
    ]);


    // =====================================================
    // CART COUNT
    // =====================================================

    const cartCount = cart.reduce(
        (total, item) =>
            total + item.quantity,
        0
    );


    // =====================================================
    // CART TOTAL
    // =====================================================

    const cartTotal = cart.reduce(
        (total, item) =>
            total +
            item.price *
            item.quantity,
        0
    );


    // =====================================================
    // ADD TO CART
    // =====================================================

    const addToCart = (product) => {

        setCart((previousCart) => {

            const existingProduct =
                previousCart.find(
                    (item) =>
                        item.id === product.id
                );

            if (existingProduct) {

                return previousCart.map(
                    (item) =>
                        item.id === product.id
                            ? {
                                ...item,
                                quantity:
                                    item.quantity + 1
                            }
                            : item
                );

            }

            return [
                ...previousCart,
                {
                    ...product,
                    quantity: 1
                }
            ];

        });

    };


    // =====================================================
    // UPDATE CART QUANTITY
    // =====================================================

    const updateQuantity = (
        productId,
        change
    ) => {

        setCart((previousCart) => {

            return previousCart
                .map((item) => {

                    if (
                        item.id === productId
                    ) {

                        return {
                            ...item,
                            quantity:
                                item.quantity +
                                change
                        };

                    }

                    return item;

                })
                .filter(
                    (item) =>
                        item.quantity > 0
                );

        });

    };


    // =====================================================
    // REMOVE CART ITEM
    // =====================================================

    const removeFromCart = (
        productId
    ) => {

        setCart(
            (previousCart) =>
                previousCart.filter(
                    (item) =>
                        item.id !== productId
                )
        );

    };


    // =====================================================
    // WISHLIST
    // =====================================================

    const toggleWishlist = (
        product
    ) => {

        setWishlist(
            (previousWishlist) => {

                const exists =
                    previousWishlist.some(
                        (item) =>
                            item.id === product.id
                    );

                if (exists) {

                    return previousWishlist.filter(
                        (item) =>
                            item.id !==
                            product.id
                    );

                }

                return [
                    ...previousWishlist,
                    product
                ];

            }
        );

    };


    // =====================================================
    // CHECKOUT
    // =====================================================

    const handleCheckout = () => {

        if (cart.length === 0) {

            alert(
                "Your cart is empty!"
            );

            return;
        }

        setShowCheckout(true);

    };


    // =====================================================
    // PLACE ORDER
    // =====================================================

    const placeOrder = () => {

        const newOrder = {

            id:
                "DL" +
                Date.now()
                    .toString()
                    .slice(-6),

            date:
                new Date()
                    .toLocaleDateString(),

            items: cart,

            total: cartTotal,

            status: "Processing"

        };


        setOrders(
            (previousOrders) => [
                newOrder,
                ...previousOrders
            ]
        );


        setCart([]);

        setShowCheckout(false);

        setShowCart(false);

        setActiveSection(
            "orders"
        );

        alert(
            "Order placed successfully! 🎉"
        );

    };


    // =====================================================
    // REORDER
    // =====================================================

    const reorder = (order) => {

        order.items.forEach(
            (item) => {

                addToCart(item);

            }
        );

        setShowCart(true);

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
            "firstName"
        );

        navigate("/login");

    };


    // =====================================================
    // NAVIGATION
    // =====================================================

    const goToSection = (
        section
    ) => {

        setActiveSection(
            section
        );

        setShowProfile(false);

        window.scrollTo({
            top: 0,
            behavior: "smooth"
        });

    };


    return (

        <div className="customer-dashboard">

            {/* =================================================
                TOP NAVBAR
            ================================================= */}

            <header className="customer-navbar">

                <div
                    className="brand"
                    onClick={() =>
                        goToSection("home")
                    }
                >

                    <div className="brand-icon">
                        🛍️
                    </div>

                    <div>
                        <strong>
                            DeluLu
                        </strong>

                        <span>
                            Cart
                        </span>
                    </div>

                </div>


                {/* SEARCH */}

                <div className="search-box">

                    <span>
                        🔍
                    </span>

                    <input
                        type="text"
                        placeholder="Search products..."
                        value={search}
                        onChange={(event) =>
                            setSearch(
                                event.target.value
                            )
                        }
                    />

                </div>


                {/* NAV ACTIONS */}

                <div className="nav-actions">

                    <button
                        className="nav-icon-button"
                        onClick={() =>
                            setShowNotifications(
                                !showNotifications
                            )
                        }
                    >
                        🔔
                    </button>


                    <button
                        className="nav-icon-button cart-nav-button"
                        onClick={() =>
                            setShowCart(true)
                        }
                    >

                        🛒

                        {cartCount > 0 && (

                            <span className="cart-count">
                                {cartCount}
                            </span>

                        )}

                    </button>


                    <button
                        className="profile-mini"
                        onClick={() =>
                            setShowProfile(
                                !showProfile
                            )
                        }
                    >

                        <div className="avatar">
                            {customerName
                                .charAt(0)
                                .toUpperCase()}
                        </div>

                        <span>
                            {customerName}
                        </span>

                    </button>

                </div>


                {/* NOTIFICATION DROPDOWN */}

                {showNotifications && (

                    <div className="notification-panel">

                        <h3>
                            Notifications
                        </h3>

                        <div className="notification-item">
                            🎉 Welcome to DeluLu Cart!
                        </div>

                        <div className="notification-item">
                            🚚 Your shopping experience
                            starts here.
                        </div>

                        <div className="notification-item">
                            💜 Discover amazing deals today.
                        </div>

                    </div>

                )}

            </header>


            {/* =================================================
                PROFILE SIDEBAR
            ================================================= */}

            {showProfile && (

                <>

                    <div
                        className="profile-overlay"
                        onClick={() =>
                            setShowProfile(false)
                        }
                    />

                    <aside className="profile-sidebar">

                        <button
                            className="close-sidebar"
                            onClick={() =>
                                setShowProfile(false)
                            }
                        >
                            ×
                        </button>


                        <div className="profile-avatar-large">

                            {customerName
                                .charAt(0)
                                .toUpperCase()}

                        </div>


                        <h2>
                            {customerName}
                        </h2>

                        <p>
                            {customerEmail}
                        </p>


                        <div className="profile-status">

                            <span>
                                ●
                            </span>

                            Active Customer

                        </div>


                        <div className="profile-menu">

                            <button
                                onClick={() =>
                                    goToSection(
                                        "home"
                                    )
                                }
                            >
                                🏠 Dashboard
                            </button>

                            <button
                                onClick={() =>
                                    goToSection(
                                        "orders"
                                    )
                                }
                            >
                                📦 My Orders
                            </button>

                            <button
                                onClick={() =>
                                    goToSection(
                                        "wishlist"
                                    )
                                }
                            >
                                ❤️ My Wishlist
                            </button>

                            <button
                                onClick={() =>
                                    setShowEditProfile(
                                        true
                                    )
                                }
                            >
                                ✏️ Edit Profile
                            </button>

                        </div>


                        <button
                            className="sidebar-logout"
                            onClick={
                                handleLogout
                            }
                        >
                            🚪 Logout
                        </button>

                    </aside>

                </>

            )}


            {/* =================================================
                MAIN CONTENT
            ================================================= */}

            <main className="customer-main">

                {/* =================================================
                    HOME
                ================================================= */}

                {activeSection === "home" && (

                    <>

                        {/* HERO */}

                        <section className="customer-hero">

                            <div className="hero-content">

                                <span className="hero-badge">
                                    ✨ Welcome back,
                                    {customerName}
                                </span>

                                <h1>
                                    Shop Your
                                    <br />

                                    <span>
                                        Happy Place.
                                    </span>
                                </h1>

                                <p>
                                    Discover trending
                                    products, amazing
                                    deals and everything
                                    you love — all in one
                                    beautiful place.
                                </p>

                                <button
                                    className="primary-button"
                                    onClick={() =>
                                        document
                                            .getElementById(
                                                "products"
                                            )
                                            ?.scrollIntoView({
                                                behavior:
                                                    "smooth"
                                            })
                                    }
                                >
                                    Start Shopping →
                                </button>

                            </div>


                            <div className="hero-visual">

                                <div className="hero-glow"></div>

                                <img
                                    src="https://images.unsplash.com/photo-1556742049-0cfed4f6a45d?w=1000"
                                    alt="Shopping"
                                />

                                <div className="floating-card card-one">
                                    🛍️
                                    <span>
                                        Happy Shopping
                                    </span>
                                </div>

                                <div className="floating-card card-two">
                                    ⭐
                                    <span>
                                        Top Rated
                                    </span>
                                </div>

                            </div>

                        </section>


                        {/* STATS */}

                        <section className="customer-stats">

                            <div className="stat-card">
                                <span>
                                    🛒
                                </span>
                                <strong>
                                    {cartCount}
                                </strong>
                                <p>
                                    Cart Items
                                </p>
                            </div>

                            <div className="stat-card">
                                <span>
                                    ❤️
                                </span>
                                <strong>
                                    {wishlist.length}
                                </strong>
                                <p>
                                    Wishlist
                                </p>
                            </div>

                            <div className="stat-card">
                                <span>
                                    📦
                                </span>
                                <strong>
                                    {orders.length}
                                </strong>
                                <p>
                                    Orders
                                </p>
                            </div>

                            <div className="stat-card">
                                <span>
                                    🎁
                                </span>
                                <strong>
                                    12
                                </strong>
                                <p>
                                    Rewards
                                </p>
                            </div>

                        </section>


                        {/* CATEGORIES */}

                        <section className="category-section">

                            <div className="section-heading">

                                <span>
                                    Explore
                                </span>

                                <h2>
                                    Shop by Category
                                </h2>

                            </div>


                            <div className="category-list">

                                {categories.map(
                                    (category) => (

                                    <button
                                        key={
                                            category.name
                                        }
                                        className={
                                            selectedCategory ===
                                            category.name
                                                ? "category-card active"
                                                : "category-card"
                                        }
                                        onClick={() => {

                                            setSelectedCategory(
                                                category.name
                                            );

                                            document
                                                .getElementById(
                                                    "products"
                                                )
                                                ?.scrollIntoView({
                                                    behavior:
                                                        "smooth"
                                                });

                                        }}
                                    >

                                        <span>
                                            {
                                                category.icon
                                            }
                                        </span>

                                        {
                                            category.name
                                        }

                                    </button>

                                ))}

                            </div>

                        </section>


                        {/* PRODUCTS */}

                        <section
                            className="products-section"
                            id="products"
                        >

                            <div className="section-heading">

                                <span>
                                    Trending Now
                                </span>

                                <h2>
                                    Discover Products
                                </h2>

                            </div>


                            <div className="product-grid">

                                {filteredProducts.length >
                                0 ? (

                                    filteredProducts.map(
                                        (product) => {

                                        const isWishlisted =
                                            wishlist.some(
                                                (item) =>
                                                    item.id ===
                                                    product.id
                                            );

                                        return (

                                            <div
                                                className="product-card"
                                                key={
                                                    product.id
                                                }
                                            >

                                                <div className="product-image-container">

                                                    <img
                                                        src={
                                                            product.image
                                                        }
                                                        alt={
                                                            product.name
                                                        }
                                                        className="product-image"
                                                    />


                                                    <button
                                                        className={
                                                            isWishlisted
                                                                ? "wishlist-button wishlisted"
                                                                : "wishlist-button"
                                                        }
                                                        onClick={() =>
                                                            toggleWishlist(
                                                                product
                                                            )
                                                        }
                                                    >
                                                        {isWishlisted
                                                            ? "♥"
                                                            : "♡"}
                                                    </button>

                                                </div>


                                                <div className="product-details">

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
                                                        Sold by{" "}
                                                        {
                                                            product.seller
                                                        }
                                                    </p>

                                                    <div className="rating">

                                                        ⭐
                                                        {
                                                            product.rating
                                                        }

                                                        <span>
                                                            (
                                                            {
                                                                product.reviews
                                                            }
                                                            )
                                                        </span>

                                                    </div>


                                                    <div className="product-bottom">

                                                        <strong>
                                                            $
                                                            {
                                                                product.price.toFixed(
                                                                    2
                                                                )
                                                            }
                                                        </strong>


                                                        <div className="product-actions">

                                                            <button
                                                                className="view-button"
                                                                onClick={() =>
                                                                    setSelectedProduct(
                                                                        product
                                                                    )
                                                                }
                                                            >
                                                                👁️
                                                            </button>

                                                            <button
                                                                className="add-cart-button"
                                                                onClick={() =>
                                                                    addToCart(
                                                                        product
                                                                    )
                                                                }
                                                            >
                                                                🛒
                                                            </button>

                                                        </div>

                                                    </div>

                                                </div>

                                            </div>

                                        );

                                    })

                                ) : (

                                    <div className="empty-state">

                                        <div>
                                            🔍
                                        </div>

                                        <h3>
                                            No products found
                                        </h3>

                                        <p>
                                            Try another
                                            search or
                                            category.
                                        </p>

                                    </div>

                                )}

                            </div>

                        </section>

                    </>

                )}


                {/* =================================================
                    ORDERS
                ================================================= */}

                {activeSection === "orders" && (

                    <section className="dashboard-section">

                        <div className="section-heading">

                            <span>
                                Your Shopping Journey
                            </span>

                            <h2>
                                My Orders
                            </h2>

                        </div>


                        {orders.length === 0 ? (

                            <div className="empty-dashboard">

                                <div>
                                    📦
                                </div>

                                <h3>
                                    No orders yet
                                </h3>

                                <p>
                                    Your order history
                                    will appear here.
                                </p>

                                <button
                                    className="primary-button"
                                    onClick={() =>
                                        goToSection(
                                            "home"
                                        )
                                    }
                                >
                                    Start Shopping
                                </button>

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

                                        <div className="order-header">

                                            <div>

                                                <strong>
                                                    Order #
                                                    {
                                                        order.id
                                                    }
                                                </strong>

                                                <p>
                                                    {
                                                        order.date
                                                    }
                                                </p>

                                            </div>

                                            <span className="order-status">
                                                {
                                                    order.status
                                                }
                                            </span>

                                        </div>


                                        <div className="order-items">

                                            {order.items.map(
                                                (item) => (

                                                <div
                                                    className="order-item"
                                                    key={
                                                        item.id
                                                    }
                                                >

                                                    <img
                                                        src={
                                                            item.image
                                                        }
                                                        alt={
                                                            item.name
                                                        }
                                                    />

                                                    <div>

                                                        <strong>
                                                            {
                                                                item.name
                                                            }
                                                        </strong>

                                                        <p>
                                                            Qty:
                                                            {
                                                                item.quantity
                                                            }
                                                        </p>

                                                    </div>

                                                </div>

                                            ))}

                                        </div>


                                        <div className="order-footer">

                                            <strong>
                                                Total: $
                                                {
                                                    order.total.toFixed(
                                                        2
                                                    )
                                                }
                                            </strong>

                                            <button
                                                className="secondary-button"
                                                onClick={() =>
                                                    reorder(
                                                        order
                                                    )
                                                }
                                            >
                                                🔁 Reorder
                                            </button>

                                        </div>

                                    </div>

                                ))}

                            </div>

                        )}

                    </section>

                )}


                {/* =================================================
                    WISHLIST
                ================================================= */}

                {activeSection === "wishlist" && (

                    <section className="dashboard-section">

                        <div className="section-heading">

                            <span>
                                Saved For Later
                            </span>

                            <h2>
                                My Wishlist ❤️
                            </h2>

                        </div>


                        {wishlist.length === 0 ? (

                            <div className="empty-dashboard">

                                <div>
                                    💜
                                </div>

                                <h3>
                                    Your wishlist is empty
                                </h3>

                                <p>
                                    Save products you love
                                    and find them here.
                                </p>

                            </div>

                        ) : (

                            <div className="product-grid">

                                {wishlist.map(
                                    (product) => (

                                    <div
                                        className="product-card"
                                        key={
                                            product.id
                                        }
                                    >

                                        <div className="product-image-container">

                                            <img
                                                src={
                                                    product.image
                                                }
                                                alt={
                                                    product.name
                                                }
                                                className="product-image"
                                            />

                                            <button
                                                className="wishlist-button wishlisted"
                                                onClick={() =>
                                                    toggleWishlist(
                                                        product
                                                    )
                                                }
                                            >
                                                ♥
                                            </button>

                                        </div>


                                        <div className="product-details">

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

                                            <div className="product-bottom">

                                                <strong>
                                                    $
                                                    {
                                                        product.price.toFixed(
                                                            2
                                                        )
                                                    }
                                                </strong>

                                                <button
                                                    className="add-cart-button"
                                                    onClick={() =>
                                                        addToCart(
                                                            product
                                                        )
                                                    }
                                                >
                                                    🛒
                                                </button>

                                            </div>

                                        </div>

                                    </div>

                                ))}

                            </div>

                        )}

                    </section>

                )}

            </main>


            {/* =================================================
                CART DRAWER
            ================================================= */}

            {showCart && (

                <>

                    <div
                        className="cart-overlay"
                        onClick={() =>
                            setShowCart(false)
                        }
                    />

                    <aside className="cart-drawer">

                        <div className="cart-header">

                            <h2>
                                🛒 Your Cart
                            </h2>

                            <button
                                onClick={() =>
                                    setShowCart(false)
                                }
                            >
                                ×
                            </button>

                        </div>


                        {cart.length === 0 ? (

                            <div className="empty-cart">

                                <div>
                                    🛒
                                </div>

                                <h3>
                                    Your cart is empty
                                </h3>

                                <p>
                                    Add something you love!
                                </p>

                            </div>

                        ) : (

                            <>

                                <div className="cart-items">

                                    {cart.map(
                                        (item) => (

                                        <div
                                            className="cart-item"
                                            key={
                                                item.id
                                            }
                                        >

                                            <img
                                                src={
                                                    item.image
                                                }
                                                alt={
                                                    item.name
                                                }
                                            />


                                            <div className="cart-item-info">

                                                <h4>
                                                    {
                                                        item.name
                                                    }
                                                </h4>

                                                <strong>
                                                    $
                                                    {
                                                        item.price.toFixed(
                                                            2
                                                        )
                                                    }
                                                </strong>


                                                <div className="quantity-controls">

                                                    <button
                                                        onClick={() =>
                                                            updateQuantity(
                                                                item.id,
                                                                -1
                                                            )
                                                        }
                                                    >
                                                        −
                                                    </button>

                                                    <span>
                                                        {
                                                            item.quantity
                                                        }
                                                    </span>

                                                    <button
                                                        onClick={() =>
                                                            updateQuantity(
                                                                item.id,
                                                                1
                                                            )
                                                        }
                                                    >
                                                        +
                                                    </button>

                                                    <button
                                                        className="remove-item"
                                                        onClick={() =>
                                                            removeFromCart(
                                                                item.id
                                                            )
                                                        }
                                                    >
                                                        🗑️
                                                    </button>

                                                </div>

                                            </div>

                                        </div>

                                    ))}

                                </div>


                                <div className="cart-footer">

                                    <div className="cart-total">

                                        <span>
                                            Total
                                        </span>

                                        <strong>
                                            $
                                            {
                                                cartTotal.toFixed(
                                                    2
                                                )
                                            }
                                        </strong>

                                    </div>


                                    <button
                                        className="checkout-button"
                                        onClick={
                                            handleCheckout
                                        }
                                    >
                                        Proceed to Checkout →
                                    </button>

                                </div>

                            </>

                        )}

                    </aside>

                </>

            )}


            {/* =================================================
                PRODUCT DETAILS MODAL
            ================================================= */}

            {selectedProduct && (

                <div className="modal-overlay">

                    <div className="product-modal">

                        <button
                            className="modal-close"
                            onClick={() =>
                                setSelectedProduct(
                                    null
                                )
                            }
                        >
                            ×
                        </button>


                        <img
                            src={
                                selectedProduct.image
                            }
                            alt={
                                selectedProduct.name
                            }
                        />


                        <div className="modal-content">

                            <span>
                                {
                                    selectedProduct.category
                                }
                            </span>

                            <h2>
                                {
                                    selectedProduct.name
                                }
                            </h2>

                            <div className="rating">
                                ⭐
                                {
                                    selectedProduct.rating
                                }

                                <small>
                                    (
                                    {
                                        selectedProduct.reviews
                                    } reviews)
                                </small>
                            </div>

                            <p>
                                {
                                    selectedProduct.description
                                }
                            </p>

                            <h3>
                                $
                                {
                                    selectedProduct.price.toFixed(
                                        2
                                    )
                                }
                            </h3>

                            <button
                                className="primary-button full-button"
                                onClick={() => {

                                    addToCart(
                                        selectedProduct
                                    );

                                    setSelectedProduct(
                                        null
                                    );

                                }}
                            >
                                🛒 Add to Cart
                            </button>

                        </div>

                    </div>

                </div>

            )}


            {/* =================================================
                CHECKOUT MODAL
            ================================================= */}

            {showCheckout && (

                <div className="modal-overlay">

                    <div className="checkout-modal">

                        <button
                            className="modal-close"
                            onClick={() =>
                                setShowCheckout(
                                    false
                                )
                            }
                        >
                            ×
                        </button>

                        <div className="checkout-icon">
                            💳
                        </div>

                        <h2>
                            Confirm Your Order
                        </h2>

                        <p>
                            You are about to place
                            an order worth
                        </p>

                        <strong className="checkout-total">
                            $
                            {
                                cartTotal.toFixed(
                                    2
                                )
                            }
                        </strong>

                        <div className="checkout-actions">

                            <button
                                className="secondary-button"
                                onClick={() =>
                                    setShowCheckout(
                                        false
                                    )
                                }
                            >
                                Cancel
                            </button>

                            <button
                                className="primary-button"
                                onClick={
                                    placeOrder
                                }
                            >
                                Confirm Order
                            </button>

                        </div>

                    </div>

                </div>

            )}


            {/* =================================================
                EDIT PROFILE MODAL
            ================================================= */}

            {showEditProfile && (

                <div className="modal-overlay">

                    <div className="profile-modal">

                        <button
                            className="modal-close"
                            onClick={() =>
                                setShowEditProfile(
                                    false
                                )
                            }
                        >
                            ×
                        </button>

                        <div className="profile-avatar-large">
                            {customerName
                                .charAt(0)
                                .toUpperCase()}
                        </div>

                        <h2>
                            Edit Profile
                        </h2>

                        <input
                            type="text"
                            value={customerName}
                            onChange={(event) =>
                                setCustomerName(
                                    event.target.value
                                )
                            }
                            placeholder="Your name"
                        />

                        <input
                            type="email"
                            value={customerEmail}
                            disabled
                        />

                        <button
                            className="primary-button full-button"
                            onClick={() => {

                                localStorage.setItem(
                                    "firstName",
                                    customerName
                                );

                                setShowEditProfile(
                                    false
                                );

                            }}
                        >
                            Save Profile
                        </button>

                    </div>

                </div>

            )}


            {/* =================================================
                FOOTER
            ================================================= */}

            <footer className="customer-footer">

                <div className="footer-brand">
                    🛍️ DeluLu Cart
                </div>

                <p>
                    Your beautiful everyday shopping
                    destination.
                </p>

                <p>
                    © 2026 DeluLu Cart. All rights reserved.
                </p>

            </footer>

        </div>

    );

}

export default CustomerDashboard;