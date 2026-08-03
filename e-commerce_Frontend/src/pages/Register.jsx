import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { registerUser } from "../service/authService";
import "./Auth.css";

function Register() {

    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        firstName: "",
        lastName: "",
        email: "",
        phoneNumber: "",
        password: "",
        role: "CUSTOMER"
    });

    const [loading, setLoading] =
        useState(false);

    const [message, setMessage] =
        useState("");


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


    const handleRoleSelect = (role) => {

        setFormData({
            ...formData,
            role
        });
    };


    const handleSubmit = async (event) => {

        event.preventDefault();

        setLoading(true);
        setMessage("");

        try {

            await registerUser(
                formData
            );

            setMessage(
                "Account created successfully! 🎉"
            );


            setTimeout(() => {

                navigate("/login");

            }, 1500);


        } catch (error) {

            console.error(
                "Registration Error:",
                error
            );


            if (
                error.response
            ) {

                setMessage(
                    error.response.data.message ||
                    "Registration failed"
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

        <div className="auth-page register-page">

            <div className="auth-glow auth-glow-one"></div>

            <div className="auth-glow auth-glow-two"></div>


            <div className="register-wrapper">


                <div className="register-top">

                    <Link
                        to="/"
                        className="auth-brand"
                    >
                        🛍️ DeluLu <b>Cart</b>
                    </Link>

                    <span>
                        Already a member?
                        <Link to="/login">
                            Sign In
                        </Link>
                    </span>

                </div>


                <div className="register-card">


                    <div className="auth-header">

                        <span className="register-icon">
                            ✨
                        </span>

                        <h2>
                            Create Your Account
                        </h2>

                        <p>
                            Join DeluLu Cart and
                            start your journey
                        </p>

                    </div>


                    <form
                        onSubmit={
                            handleSubmit
                        }
                    >


                        <div className="form-row">

                            <div className="form-group">

                                <label>
                                    First Name
                                </label>

                                <input
                                    type="text"
                                    name="firstName"
                                    placeholder="First name"
                                    value={
                                        formData.firstName
                                    }
                                    onChange={
                                        handleChange
                                    }
                                    required
                                />

                            </div>


                            <div className="form-group">

                                <label>
                                    Last Name
                                </label>

                                <input
                                    type="text"
                                    name="lastName"
                                    placeholder="Last name"
                                    value={
                                        formData.lastName
                                    }
                                    onChange={
                                        handleChange
                                    }
                                    required
                                />

                            </div>

                        </div>


                        <div className="form-row">

                            <div className="form-group">

                                <label>
                                    Email
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
                                    Phone Number
                                </label>

                                <input
                                    type="tel"
                                    name="phoneNumber"
                                    placeholder="Phone number"
                                    value={
                                        formData.phoneNumber
                                    }
                                    onChange={
                                        handleChange
                                    }
                                />

                            </div>

                        </div>


                        <div className="form-group">

                            <label>
                                Password
                            </label>

                            <input
                                type="password"
                                name="password"
                                placeholder="Create a strong password"
                                value={
                                    formData.password
                                }
                                onChange={
                                    handleChange
                                }
                                required
                            />

                        </div>


                        <div className="role-section">

                            <label>
                                Choose Account Type
                            </label>


                            <div className="role-options">


                                <button
                                    type="button"
                                    className={
                                        formData.role ===
                                        "CUSTOMER"
                                            ? "role-option active"
                                            : "role-option"
                                    }
                                    onClick={() =>
                                        handleRoleSelect(
                                            "CUSTOMER"
                                        )
                                    }
                                >

                                    <span>
                                        🛍️
                                    </span>

                                    <div>

                                        <strong>
                                            Customer
                                        </strong>

                                        <small>
                                            Shop amazing products
                                        </small>

                                    </div>

                                    {
                                        formData.role ===
                                        "CUSTOMER"
                                            ? "✓"
                                            : ""
                                    }

                                </button>


                                <button
                                    type="button"
                                    className={
                                        formData.role ===
                                        "SELLER"
                                            ? "role-option active"
                                            : "role-option"
                                    }
                                    onClick={() =>
                                        handleRoleSelect(
                                            "SELLER"
                                        )
                                    }
                                >

                                    <span>
                                        🏪
                                    </span>

                                    <div>

                                        <strong>
                                            Seller
                                        </strong>

                                        <small>
                                            Grow your business
                                        </small>

                                    </div>

                                    {
                                        formData.role ===
                                        "SELLER"
                                            ? "✓"
                                            : ""
                                    }

                                </button>

                            </div>

                        </div>


                        <button
                            type="submit"
                            className="auth-submit"
                            disabled={loading}
                        >

                            {loading
                                ? "Creating Account..."
                                : `Create ${
                                    formData.role ===
                                    "SELLER"
                                        ? "Seller"
                                        : "Customer"
                                  } Account →`
                            }

                        </button>

                    </form>


                    {message && (

                        <div
                            className={
                                message.includes(
                                    "successfully"
                                )
                                    ? "auth-message success"
                                    : "auth-message error"
                            }
                        >

                            {message}

                        </div>

                    )}

                </div>

            </div>

        </div>
    );
}

export default Register;