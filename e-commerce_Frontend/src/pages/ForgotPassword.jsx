import { useState } from "react";
import { Link } from "react-router-dom";
import axios from "axios";
import "./Auth.css";

function ForgotPassword() {

    const [email, setEmail] = useState("");

    const [message, setMessage] = useState("");

    const [error, setError] = useState("");

    const [loading, setLoading] = useState(false);


    const handleSubmit = async (event) => {

        event.preventDefault();

        setMessage("");
        setError("");
        setLoading(true);

        try {

            const response = await axios.post(
                "http://localhost:8080/api/auth/forgot-password",
                {
                    email: email
                }
            );

            console.log(
                "Forgot Password Response:",
                response.data
            );

            setMessage(
                "Password reset link has been sent to your email. Please check your inbox."
            );

            setEmail("");

        } catch (error) {

            console.error(
                "Forgot Password Error:",
                error
            );

            if (error.response) {

                setError(
                    error.response.data.message ||
                    "Unable to process forgot password request."
                );

            } else {

                setError(
                    "Unable to connect to server."
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


            <div className="auth-form-container">

                <div className="auth-card">


                    <div className="mobile-brand">

                        🛍️ DeluLu Cart

                    </div>


                    <div className="auth-header">

                        <h2>
                            Forgot Password 🔐
                        </h2>

                        <p>
                            Enter your registered email
                            to reset your password
                        </p>

                    </div>


                    <form
                        onSubmit={handleSubmit}
                    >

                        <div className="form-group">

                            <label>
                                Email Address
                            </label>

                            <input
                                type="email"
                                placeholder="you@example.com"
                                value={email}
                                onChange={(event) =>
                                    setEmail(
                                        event.target.value
                                    )
                                }
                                required
                            />

                        </div>


                        <button
                            type="submit"
                            className="auth-submit"
                            disabled={loading}
                        >

                            {loading
                                ? "Sending..."
                                : "Send Reset Link →"
                            }

                        </button>

                    </form>


                    {message && (

                        <div className="auth-message success">

                            {message}

                        </div>

                    )}


                    {error && (

                        <div className="auth-message error">

                            {error}

                        </div>

                    )}


                    <div className="auth-footer">

                        <span>
                            Remember your password?
                        </span>

                        <Link to="/login">
                            Back to Login
                        </Link>

                    </div>


                    <div className="security-note">

                        🔒 Your account is secure

                    </div>

                </div>

            </div>

        </div>
    );
}

export default ForgotPassword;