import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { loginUser } from "../service/authService";
import "./Auth.css";

function Login() {

    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        email: "",
        password: ""
    });

    const [showPassword, setShowPassword] =
        useState(false);

    const [message, setMessage] =
        useState("");

    const [loading, setLoading] =
        useState(false);


    const handleChange = (event) => {

        const {
            name,
            value
        } = event.target;

        setFormData({
            ...formData,
            [name]: value
        });

        setMessage("");
    };


    const handleSubmit = async (event) => {

        event.preventDefault();

        setLoading(true);
        setMessage("");

        try {

            const response =
                await loginUser(formData);

            console.log(
                "Login Response:",
                response
            );


            localStorage.setItem(
                "token",
                response.token
            );


            const userRole =
                response.role?.toUpperCase();


            if (userRole) {

                localStorage.setItem(
                    "role",
                    userRole
                );
            }


            localStorage.setItem(
                "email",
                formData.email
            );


            setMessage(
                "Welcome back to DeluLu Cart! 🎉"
            );


            setTimeout(() => {

                if (
                    userRole === "ADMIN"
                ) {

                    navigate(
                        "/admin-dashboard"
                    );

                } else if (
                    userRole === "SELLER"
                ) {

                    navigate(
                        "/seller-dashboard"
                    );

                } else {

                    navigate(
                        "/customer-dashboard"
                    );
                }

            }, 1000);


        } catch (error) {

            console.error(
                "Login Error:",
                error
            );


            if (
                error.response
            ) {

                setMessage(
                    error.response.data.message ||
                    "Invalid email or password"
                );

            } else {

                setMessage(
                    "Unable to connect to server"
                );
            }

        } finally {

            setLoading(false);
        }
    };


    return (

        <div className="auth-page">

            <div className="auth-glow auth-glow-one"></div>

            <div className="auth-glow auth-glow-two"></div>


            <div className="auth-wrapper">


                {/* LEFT */}

                <div className="auth-showcase">

                    <Link
                        to="/"
                        className="auth-brand"
                    >
                        🛍️ DeluLu <b>Cart</b>
                    </Link>


                    <div className="showcase-content">

                        <span>
                            ✨ WELCOME BACK
                        </span>

                        <h1>
                            Your shopping
                            journey continues
                            <strong>
                                here.
                            </strong>
                        </h1>

                        <p>
                            Sign in to discover
                            products, manage your
                            cart and enjoy a smarter
                            shopping experience.
                        </p>

                    </div>


                    <div className="showcase-floating">

                        🛒

                        <span>
                            Shop smarter.
                            Live better.
                        </span>

                    </div>

                </div>


                {/* FORM */}

                <div className="auth-form-container">

                    <div className="auth-card">


                        <div className="mobile-brand">

                            🛍️ DeluLu Cart

                        </div>


                        <div className="auth-header">

                            <h2>
                                Welcome Back 👋
                            </h2>

                            <p>
                                Login to your account
                            </p>

                        </div>


                        <form
                            onSubmit={
                                handleSubmit
                            }
                        >


                            <div className="form-group">

                                <label>
                                    Email Address
                                </label>

                                <input
                                    type="email"
                                    name="email"
                                    placeholder="you@example.com"
                                    value={
                                        formData.email
                                    }
                                    onChange={
                                        handleChange
                                    }
                                    required
                                />

                            </div>

<div className="form-group">

    <label>
        Password
    </label>

    <div className="password-field">

        <input
            type={
                showPassword
                    ? "text"
                    : "password"
            }
            name="password"
            placeholder="Enter your password"
            value={formData.password}
            onChange={handleChange}
            required
        />

        <button
            type="button"
            onClick={() =>
                setShowPassword(
                    !showPassword
                )
            }
        >
            {
                showPassword
                    ? "🙈"
                    : "👁️"
            }
        </button>

    </div>


    {/* FORGOT PASSWORD */}

    <div className="forgot-password-link">

        <Link to="/forgot-password">
            Forgot Password?
        </Link>

    </div>

</div>



                            <button
                                type="submit"
                                className="auth-submit"
                                disabled={loading}
                            >

                                {loading
                                    ? "Signing In..."
                                    : "Sign In →"
                                }

                            </button>

                        </form>


                        {message && (

                            <div
                                className={
                                    message.includes(
                                        "Welcome"
                                    )
                                        ? "auth-message success"
                                        : "auth-message error"
                                }
                            >

                                {message}

                            </div>

                        )}


                        <div className="auth-footer">

                            <span>
                                Don't have an account?
                            </span>

                            <Link to="/register">
                                Create Account
                            </Link>

                        </div>


                        <div className="security-note">

                            🔒 Secure & encrypted
                            authentication

                        </div>

                    </div>

                </div>

            </div>

        </div>
    );
}

export default Login;