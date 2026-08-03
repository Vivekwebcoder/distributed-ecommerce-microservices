import { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./AdminDashboard.css";

function AdminDashboard() {

    const navigate = useNavigate();

    const [sidebarOpen, setSidebarOpen] = useState(false);
    const [activeMenu, setActiveMenu] = useState("Dashboard");
    const [search, setSearch] = useState("");

    const [stats] = useState([
        {
            title: "Total Sales",
            value: "$48,290",
            icon: "💰",
            growth: "+12.5%"
        },
        {
            title: "Total Orders",
            value: "1,284",
            icon: "🛍️",
            growth: "+8.2%"
        },
        {
            title: "Total Customers",
            value: "8,549",
            icon: "👥",
            growth: "+15.4%"
        },
        {
            title: "Total Products",
            value: "1,842",
            icon: "📦",
            growth: "+6.8%"
        }
    ]);


    const [recentOrders] = useState([
        {
            id: "#ORD-10245",
            customer: "Arun Kumar",
            product: "Premium Sneakers",
            amount: "$129.99",
            status: "Completed"
        },
        {
            id: "#ORD-10244",
            customer: "Priya Sharma",
            product: "Wireless Headphones",
            amount: "$89.99",
            status: "Pending"
        },
        {
            id: "#ORD-10243",
            customer: "Kavin Raj",
            product: "Smart Watch",
            amount: "$199.99",
            status: "Completed"
        },
        {
            id: "#ORD-10242",
            customer: "Meena Devi",
            product: "Leather Handbag",
            amount: "$79.99",
            status: "Cancelled"
        }
    ]);


    const menuItems = [
        {
            name: "Dashboard",
            icon: "📊"
        },
        {
            name: "Users",
            icon: "👥"
        },
        {
            name: "Products",
            icon: "📦"
        },
        {
            name: "Categories",
            icon: "🗂️"
        },
        {
            name: "Brands",
            icon: "🏷️"
        },
        {
            name: "Orders",
            icon: "🛒"
        },
        {
            name: "Coupons",
            icon: "🎟️"
        },
        {
            name: "Inventory",
            icon: "📋"
        },
        {
            name: "Reviews",
            icon: "⭐"
        },
        {
            name: "Reports",
            icon: "📈"
        }
    ];


    const handleMenuClick = (menu) => {

        setActiveMenu(menu);

        setSidebarOpen(false);

    };


    const handleLogout = () => {

        localStorage.removeItem("token");
        localStorage.removeItem("role");
        localStorage.removeItem("email");

        navigate("/login");
    };


    return (

        <div className="admin-dashboard">


            {/* =========================================
                MOBILE OVERLAY
            ========================================= */}

            {sidebarOpen && (

                <div
                    className="admin-overlay"
                    onClick={() =>
                        setSidebarOpen(false)
                    }
                ></div>

            )}


            {/* =========================================
                SIDEBAR
            ========================================= */}

            <aside
                className={
                    sidebarOpen
                        ? "admin-sidebar open"
                        : "admin-sidebar"
                }
            >


                {/* LOGO */}

                <div className="admin-logo">

                    <div className="admin-logo-icon">
                        🛍️
                    </div>

                    <div className="admin-logo-text">

                        <span>
                            DeluLu
                        </span>

                        <strong>
                            Cart
                        </strong>

                    </div>

                    <button
                        className="sidebar-close"
                        onClick={() =>
                            setSidebarOpen(false)
                        }
                    >
                        ✕
                    </button>

                </div>


                {/* PROFILE */}

                <div className="admin-profile">

                    <div className="admin-avatar">
                        👑
                    </div>

                    <div className="admin-profile-info">

                        <h4>
                            Administrator
                        </h4>

                        <p>
                            {localStorage.getItem("email") ||
                                "admin@delulu.com"}
                        </p>

                    </div>

                </div>


                {/* NAVIGATION */}

                <div className="sidebar-section-title">
                    MAIN MENU
                </div>


                <nav className="admin-nav">

                    {menuItems.map((item) => (

                        <button
                            key={item.name}
                            className={
                                activeMenu === item.name
                                    ? "admin-nav-item active"
                                    : "admin-nav-item"
                            }
                            onClick={() =>
                                handleMenuClick(
                                    item.name
                                )
                            }
                        >

                            <span className="admin-nav-icon">
                                {item.icon}
                            </span>

                            <span className="admin-nav-text">
                                {item.name}
                            </span>

                            {activeMenu === item.name && (

                                <span className="active-indicator">
                                    →
                                </span>

                            )}

                        </button>

                    ))}

                </nav>


                {/* SIDEBAR BOTTOM */}

                <div className="sidebar-bottom">

                    <button className="sidebar-help">

                        <span>
                            💡
                        </span>

                        <div>

                            <strong>
                                Need Help?
                            </strong>

                            <small>
                                Admin support
                            </small>

                        </div>

                    </button>


                    <button
                        className="admin-logout"
                        onClick={handleLogout}
                    >

                        <span>
                            🚪
                        </span>

                        Logout

                    </button>

                </div>

            </aside>


            {/* =========================================
                MAIN CONTENT
            ========================================= */}

            <main className="admin-main">


                {/* HEADER */}

                <header className="admin-header">


                    <div className="admin-header-left">

                        <button
                            className="admin-menu-button"
                            onClick={() =>
                                setSidebarOpen(true)
                            }
                        >
                            ☰
                        </button>


                        <div>

                            <span className="header-label">
                                ADMIN CONTROL CENTER
                            </span>

                            <h1>
                                {activeMenu}
                            </h1>

                            <p>
                                Manage your DeluLu Cart
                                platform from one place.
                            </p>

                        </div>

                    </div>


                    <div className="admin-header-actions">

                        <div className="admin-search">

                            <span>
                                🔍
                            </span>

                            <input
                                type="text"
                                placeholder="Search..."
                                value={search}
                                onChange={(event) =>
                                    setSearch(
                                        event.target.value
                                    )
                                }
                            />

                        </div>


                        <button className="notification-button">

                            🔔

                            <span className="notification-dot"></span>

                        </button>


                        <div className="header-avatar">
                            👑
                        </div>

                    </div>

                </header>


                {/* =========================================
                    DASHBOARD CONTENT
                ========================================= */}

                {activeMenu === "Dashboard" ? (

                    <>


                        {/* WELCOME BANNER */}

                        <section className="welcome-banner">

                            <div>

                                <span>
                                    ✨ GOOD TO SEE YOU, ADMIN
                                </span>

                                <h2>
                                    Welcome back to
                                    <strong>
                                        DeluLu Cart
                                    </strong>
                                </h2>

                                <p>
                                    Here's what's happening
                                    with your store today.
                                </p>

                            </div>

                            <div className="welcome-illustration">
                                🛍️
                            </div>

                        </section>


                        {/* STATS */}

                        <section className="admin-stats-grid">

                            {stats.map((stat) => (

                                <div
                                    className="admin-stat-card"
                                    key={stat.title}
                                >

                                    <div className="stat-icon">
                                        {stat.icon}
                                    </div>

                                    <div className="stat-content">

                                        <span>
                                            {stat.title}
                                        </span>

                                        <h3>
                                            {stat.value}
                                        </h3>

                                        <small>
                                            <b>
                                                {stat.growth}
                                            </b>
                                            {" "}from last month
                                        </small>

                                    </div>

                                </div>

                            ))}

                        </section>


                        {/* MAIN GRID */}

                        <section className="admin-content-grid">


                            {/* RECENT ORDERS */}

                            <div className="admin-card orders-card">

                                <div className="admin-card-header">

                                    <div>

                                        <span className="card-label">
                                            ORDER MANAGEMENT
                                        </span>

                                        <h2>
                                            Recent Orders
                                        </h2>

                                    </div>

                                    <button
                                        className="view-all-button"
                                        onClick={() =>
                                            handleMenuClick(
                                                "Orders"
                                            )
                                        }
                                    >
                                        View All →
                                    </button>

                                </div>


                                <div className="admin-table-wrapper">

                                    <table className="admin-table">

                                        <thead>

                                            <tr>

                                                <th>
                                                    Order ID
                                                </th>

                                                <th>
                                                    Customer
                                                </th>

                                                <th>
                                                    Product
                                                </th>

                                                <th>
                                                    Amount
                                                </th>

                                                <th>
                                                    Status
                                                </th>

                                            </tr>

                                        </thead>


                                        <tbody>

                                            {recentOrders.map(
                                                (order) => (

                                                    <tr
                                                        key={
                                                            order.id
                                                        }
                                                    >

                                                        <td>
                                                            <strong>
                                                                {
                                                                    order.id
                                                                }
                                                            </strong>
                                                        </td>

                                                        <td>
                                                            {
                                                                order.customer
                                                            }
                                                        </td>

                                                        <td>
                                                            {
                                                                order.product
                                                            }
                                                        </td>

                                                        <td>
                                                            <strong>
                                                                {
                                                                    order.amount
                                                                }
                                                            </strong>
                                                        </td>

                                                        <td>

                                                            <span
                                                                className={`status-badge status-${order.status.toLowerCase()}`}
                                                            >
                                                                {
                                                                    order.status
                                                                }
                                                            </span>

                                                        </td>

                                                    </tr>

                                                )
                                            )}

                                        </tbody>

                                    </table>

                                </div>

                            </div>


                            {/* RIGHT SIDE */}

                            <div className="admin-side-column">


                                {/* SALES REPORT */}

                                <div className="admin-card">

                                    <div className="admin-card-header">

                                        <div>

                                            <span className="card-label">
                                                ANALYTICS
                                            </span>

                                            <h2>
                                                Sales Overview
                                            </h2>

                                        </div>

                                        <span className="period-badge">
                                            This Month
                                        </span>

                                    </div>


                                    <div className="sales-overview">

                                        <div className="sales-total">
                                            $48,290
                                        </div>

                                        <div className="sales-growth">
                                            ↑ 12.5%
                                        </div>

                                    </div>


                                    <div className="fake-chart">

                                        <div style={{
                                            height: "35%"
                                        }}></div>

                                        <div style={{
                                            height: "55%"
                                        }}></div>

                                        <div style={{
                                            height: "45%"
                                        }}></div>

                                        <div style={{
                                            height: "75%"
                                        }}></div>

                                        <div style={{
                                            height: "60%"
                                        }}></div>

                                        <div style={{
                                            height: "90%"
                                        }}></div>

                                        <div style={{
                                            height: "70%"
                                        }}></div>

                                    </div>

                                    <div className="chart-labels">

                                        <span>
                                            Mon
                                        </span>

                                        <span>
                                            Tue
                                        </span>

                                        <span>
                                            Wed
                                        </span>

                                        <span>
                                            Thu
                                        </span>

                                        <span>
                                            Fri
                                        </span>

                                        <span>
                                            Sat
                                        </span>

                                        <span>
                                            Sun
                                        </span>

                                    </div>

                                </div>


                                {/* QUICK ACTIONS */}

                                <div className="admin-card">

                                    <div className="admin-card-header">

                                        <div>

                                            <span className="card-label">
                                                SHORTCUTS
                                            </span>

                                            <h2>
                                                Quick Actions
                                            </h2>

                                        </div>

                                    </div>


                                    <div className="quick-actions">

                                        <button
                                            onClick={() =>
                                                handleMenuClick(
                                                    "Users"
                                                )
                                            }
                                        >
                                            👥
                                            <span>
                                                Manage Users
                                            </span>
                                        </button>


                                        <button
                                            onClick={() =>
                                                handleMenuClick(
                                                    "Products"
                                                )
                                            }
                                        >
                                            📦
                                            <span>
                                                Manage Products
                                            </span>
                                        </button>


                                        <button
                                            onClick={() =>
                                                handleMenuClick(
                                                    "Orders"
                                                )
                                            }
                                        >
                                            🛒
                                            <span>
                                                Manage Orders
                                            </span>
                                        </button>


                                        <button
                                            onClick={() =>
                                                handleMenuClick(
                                                    "Reports"
                                                )
                                            }
                                        >
                                            📈
                                            <span>
                                                View Reports
                                            </span>
                                        </button>

                                    </div>

                                </div>

                            </div>

                        </section>


                        {/* REPORT SUMMARY */}

                        <section className="report-summary">

                            <div className="report-summary-header">

                                <div>

                                    <span className="card-label">
                                        BUSINESS INSIGHTS
                                    </span>

                                    <h2>
                                        Store Performance
                                    </h2>

                                </div>

                            </div>


                            <div className="report-grid">

                                <div className="report-card">

                                    <span>
                                        Best Selling Product
                                    </span>

                                    <strong>
                                        Premium Sneakers
                                    </strong>

                                    <small>
                                        248 units sold
                                    </small>

                                </div>


                                <div className="report-card">

                                    <span>
                                        Top Customer
                                    </span>

                                    <strong>
                                        Arun Kumar
                                    </strong>

                                    <small>
                                        $2,450 total spent
                                    </small>

                                </div>


                                <div className="report-card">

                                    <span>
                                        Monthly Revenue
                                    </span>

                                    <strong>
                                        $18,920
                                    </strong>

                                    <small>
                                        ↑ 14.2% growth
                                    </small>

                                </div>


                                <div className="report-card">

                                    <span>
                                        Pending Orders
                                    </span>

                                    <strong>
                                        86
                                    </strong>

                                    <small>
                                        Need attention
                                    </small>

                                </div>

                            </div>

                        </section>

                    </>

                ) : (

                    /* =========================================
                       OTHER MODULE PLACEHOLDER
                    ========================================= */

                    <section className="module-placeholder">

                        <div className="placeholder-icon">
                            {menuItems.find(
                                (item) =>
                                    item.name ===
                                    activeMenu
                            )?.icon}
                        </div>

                        <h2>
                            {activeMenu} Management
                        </h2>

                        <p>
                            The {activeMenu.toLowerCase()}
                            management module is ready
                            to be connected with your
                            backend API.
                        </p>

                        <button
                            className="admin-primary-button"
                            onClick={() =>
                                setActiveMenu(
                                    "Dashboard"
                                )
                            }
                        >
                            ← Back to Dashboard
                        </button>

                    </section>

                )}

            </main>

        </div>
    );
}

export default AdminDashboard;