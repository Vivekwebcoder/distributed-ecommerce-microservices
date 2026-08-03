import { Link } from "react-router-dom";
import "./Home.css";

function Home() {

    return (

        <div className="delulu-home">

            {/* =========================
                NAVBAR
            ========================== */}

            <nav className="home-navbar">

                <Link
                    to="/"
                    className="home-logo"
                >

                    <span className="logo-icon">
                        🛍️
                    </span>

                    <span>
                        DeluLu <b>Cart</b>
                    </span>

                </Link>


                <div className="home-nav-links">

                    <a href="#home">
                        Home
                    </a>

                    <a href="#features">
                        Features
                    </a>

                    <a href="#categories">
                        Categories
                    </a>

                    <a href="#about">
                        About
                    </a>

                </div>


                <div className="home-nav-actions">

                    <Link
                        to="/login"
                        className="nav-login"
                    >
                        Login
                    </Link>

                    <Link
                        to="/register"
                        className="nav-register"
                    >
                        Get Started
                    </Link>

                </div>

            </nav>


            {/* =========================
                HERO SECTION
            ========================== */}

            <section
                className="home-hero"
                id="home"
            >

                <div className="hero-blob blob-one"></div>

                <div className="hero-blob blob-two"></div>


                <div className="hero-content">

                    <div className="hero-badge">

                        ✨ The future of shopping is here

                    </div>


                    <h1>

                        Shop Your
                        <span>
                            Way.
                        </span>

                        <br />

                        Live Your
                        <span>
                            Style.
                        </span>

                    </h1>


                    <p>

                        Welcome to <strong>DeluLu Cart</strong> —
                        your modern shopping destination where
                        fashion, technology and lifestyle come
                        together in one beautiful experience.

                    </p>


                    <div className="hero-buttons">

                        <Link
                            to="/register"
                            className="primary-hero-button"
                        >

                            Start Shopping

                            <span>
                                →
                            </span>

                        </Link>


                        <a
                            href="#categories"
                            className="secondary-hero-button"
                        >

                            Explore Categories

                        </a>

                    </div>


                    <div className="hero-stats">

                        <div>

                            <strong>
                                10K+
                            </strong>

                            <span>
                                Products
                            </span>

                        </div>


                        <div>

                            <strong>
                                5K+
                            </strong>

                            <span>
                                Happy Customers
                            </span>

                        </div>


                        <div>

                            <strong>
                                99%
                            </strong>

                            <span>
                                Satisfaction
                            </span>

                        </div>

                    </div>

                </div>


                {/* =========================
                    HERO VISUAL
                ========================== */}

                <div className="hero-visual">

                    <div className="visual-glow"></div>


                    <div className="floating-card card-top">

                        <span>
                            🔥
                        </span>

                        <div>

                            <strong>
                                Trending Now
                            </strong>

                            <small>
                                New collections
                            </small>

                        </div>

                    </div>


                    <div className="shopping-circle">

                        <div className="circle-inner">

                            🛍️

                        </div>

                    </div>


                    <div className="floating-card card-bottom">

                        <span>
                            ⭐
                        </span>

                        <div>

                            <strong>
                                Loved by shoppers
                            </strong>

                            <small>
                                4.9 / 5 rating
                            </small>

                        </div>

                    </div>


                    <div className="visual-product product-one">
                        👟
                    </div>

                    <div className="visual-product product-two">
                        🎧
                    </div>

                    <div className="visual-product product-three">
                        ⌚
                    </div>

                </div>

            </section>


            {/* =========================
                FEATURES
            ========================== */}

            <section
                className="features-section"
                id="features"
            >

                <div className="section-heading">

                    <span>
                        Why DeluLu Cart?
                    </span>

                    <h2>
                        Shopping made beautifully simple
                    </h2>

                    <p>
                        Everything you need for a smooth,
                        secure and enjoyable shopping journey.
                    </p>

                </div>


                <div className="features-grid">

                    <div className="feature-card">

                        <div className="feature-icon">
                            🚚
                        </div>

                        <h3>
                            Fast Delivery
                        </h3>

                        <p>
                            Get your favourite products
                            delivered quickly and safely
                            to your doorstep.
                        </p>

                    </div>


                    <div className="feature-card">

                        <div className="feature-icon">
                            🔐
                        </div>

                        <h3>
                            Secure Shopping
                        </h3>

                        <p>
                            Your account and personal
                            information are protected
                            with secure authentication.
                        </p>

                    </div>


                    <div className="feature-card">

                        <div className="feature-icon">
                            💳
                        </div>

                        <h3>
                            Easy Payments
                        </h3>

                        <p>
                            Enjoy a simple and seamless
                            checkout experience with
                            secure payment options.
                        </p>

                    </div>


                    <div className="feature-card">

                        <div className="feature-icon">
                            💖
                        </div>

                        <h3>
                            Loved Products
                        </h3>

                        <p>
                            Discover products selected
                            to match your style and
                            everyday lifestyle.
                        </p>

                    </div>

                </div>

            </section>


            {/* =========================
                CATEGORIES
            ========================== */}

            <section
                className="categories-section"
                id="categories"
            >

                <div className="section-heading">

                    <span>
                        Explore
                    </span>

                    <h2>
                        Shop your favourite world
                    </h2>

                </div>


                <div className="category-grid">

                    <div className="category-card fashion">

                        <div>
                            👗
                        </div>

                        <h3>
                            Fashion
                        </h3>

                        <p>
                            Discover your style
                        </p>

                        <Link to="/register">
                            Explore →
                        </Link>

                    </div>


                    <div className="category-card electronics">

                        <div>
                            🎧
                        </div>

                        <h3>
                            Electronics
                        </h3>

                        <p>
                            Smart life starts here
                        </p>

                        <Link to="/register">
                            Explore →
                        </Link>

                    </div>


                    <div className="category-card home-category">

                        <div>
                            🏠
                        </div>

                        <h3>
                            Home & Living
                        </h3>

                        <p>
                            Make your space beautiful
                        </p>

                        <Link to="/register">
                            Explore →
                        </Link>

                    </div>


                    <div className="category-card beauty">

                        <div>
                            ✨
                        </div>

                        <h3>
                            Beauty
                        </h3>

                        <p>
                            Feel confident every day
                        </p>

                        <Link to="/register">
                            Explore →
                        </Link>

                    </div>

                </div>

            </section>


            {/* =========================
                ROLE SECTION
            ========================== */}

            <section
                className="role-section"
                id="about"
            >

                <div className="section-heading">

                    <span>
                        One Platform
                    </span>

                    <h2>
                        Built for everyone
                    </h2>

                    <p>
                        Whether you love shopping or
                        growing your business, DeluLu Cart
                        has a place for you.
                    </p>

                </div>


                <div className="role-grid">

                    <div className="role-box customer-box">

                        <span className="role-box-icon">
                            🛍️
                        </span>

                        <h3>
                            Shop as a Customer
                        </h3>

                        <p>
                            Discover products, explore
                            categories, manage your cart
                            and enjoy an amazing shopping
                            experience.
                        </p>

                        <Link to="/register">
                            Join as Customer →
                        </Link>

                    </div>


                    <div className="role-box seller-box">

                        <span className="role-box-icon">
                            🏪
                        </span>

                        <h3>
                            Grow as a Seller
                        </h3>

                        <p>
                            Create your store, manage
                            products, track orders and
                            grow your business with DeluLu Cart.
                        </p>

                        <Link to="/register">
                            Join as Seller →
                        </Link>

                    </div>

                </div>

            </section>


            {/* =========================
                CTA
            ========================== */}

            <section className="cta-section">

                <div>

                    <span>
                        ✨ Your shopping journey starts here
                    </span>

                    <h2>
                        Ready to experience
                        <br />
                        DeluLu Cart?
                    </h2>

                    <p>
                        Create your account and discover
                        a new way to shop online.
                    </p>

                    <Link
                        to="/register"
                        className="cta-button"
                    >
                        Create Your Account →
                    </Link>

                </div>

            </section>


            {/* =========================
                FOOTER
            ========================== */}

            <footer className="home-footer">

                <div className="footer-logo">

                    🛍️ DeluLu Cart

                </div>

                <p>
                    Shop. Discover. Enjoy.
                </p>

                <div className="footer-links">

                    <Link to="/login">
                        Login
                    </Link>

                    <Link to="/register">
                        Register
                    </Link>

                </div>

                <div className="footer-copy">

                    © 2026 DeluLu Cart. All rights reserved.

                </div>

            </footer>

        </div>
    );
}

export default Home;