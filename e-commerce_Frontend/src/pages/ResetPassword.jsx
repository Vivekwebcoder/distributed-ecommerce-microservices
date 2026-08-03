import { useState } from "react";
import { Link, useSearchParams, useNavigate } from "react-router-dom";
import { resetPassword } from "../service/authService";
import "./Auth.css";

function ResetPassword() {

    const [searchParams] = useSearchParams();
    const navigate = useNavigate();

    // Get token from URL
    // Example:
    // /reset-password?token=abc123
    const token = searchParams.get("token");

    const [newPassword, setNewPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");

    const [message, setMessage] = useState("");
    const [loading, setLoading] = useState(false);

    const [showPassword, setShowPassword] =
        useState(false);

    const [showConfirmPassword, setShowConfirmPassword] =
        useState(false);


    const handleSubmit = async (event) => {

        event.preventDefault();

        setMessage("");


        // Check token
        if (!token) {

            setMessage(
                "Invalid or missing password reset token."
            );

            return;
        }


        // Check password match
        if (newPassword !== confirmPassword) {

            setMessage(
                "Passwords do not match."
            );

            return;
        }


        // Check password length
        if (newPassword.length < 8) {

            setMessage(
                "Password must contain at least 8 characters."
            );

            return;
        }


        setLoading(true);


        try {

            const response = await resetPassword({

                token: token,

                newPassword: newPassword

            });


            setMessage(
                response.message ||
                "Password reset successfully."
            );


            // After 2 seconds go to login
            setTimeout(() => {

                navigate("/login");

            }, 2000);


        } catch (error) {

            console.error(
                "Reset Password Error:",
                error
            );


            if (error.response) {

                setMessage(
                    error.response.data.message ||
                    "Unable to reset password."
                );

            } else {

                setMessage(
                    "Unable to connect to server."
                );
            }

        } finally {

            setLoading(false);
        }
    };


    return (

        <div className="auth-page">

            {/* Background Glow */}

            <div className="auth-glow auth-glow-one"></div>

            <div className="auth-glow auth-glow-two"></div>


            <div className="auth-wrapper">


                {/* =========================
                    LEFT SIDE
                ========================== */}

                <div className="auth-showcase">

                    <Link
                        to="/"
                        className="auth-brand"
                    >
                        🛍️ DeluLu <b>Cart</b>
                    </Link>


                    <div className="showcase-content">

                        <span>
                            🔐 PASSWORD RESET
                        </span>


                        <h1>

                            Create your
                            
                            <strong>
                                new password.
                            </strong>

                        </h1>


                        <p>

                            Choose a strong password
                            to keep your DeluLu Cart
                            account secure.

                        </p>

                    </div>


                    <div className="showcase-floating">

                        🔒

                        <span>

                            Stay secure.
                            Shop with confidence.

                        </span>

                    </div>

                </div>


                {/* =========================
                    RIGHT SIDE
                ========================== */}

                <div className="auth-form-container">

                    <div className="auth-card">


                        {/* Mobile Brand */}

                        <div className="mobile-brand">

                            🛍️ DeluLu Cart

                        </div>


                        {/* Header */}

                        <div className="auth-header">

                            <h2>

                                Reset Password 🔑

                            </h2>


                            <p>

                                Enter your new password

                            </p>

                        </div>


                        <form
                            onSubmit={handleSubmit}
                        >


                            {/* New Password */}

                            <div className="form-group">

                                <label>

                                    New Password

                                </label>


                                <div className="password-field">

                                    <input
                                        type={
                                            showPassword
                                                ? "text"
                                                : "password"
                                        }
                                        placeholder="Enter new password"
                                        value={
                                            newPassword
                                        }
                                        onChange={
                                            (event) =>
                                                setNewPassword(
                                                    event.target.value
                                                )
                                        }
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

                            </div>


                            {/* Confirm Password */}

                            <div className="form-group">

                                <label>

                                    Confirm Password

                                </label>


                                <div className="password-field">

                                    <input
                                        type={
                                            showConfirmPassword
                                                ? "text"
                                                : "password"
                                        }
                                        placeholder="Confirm new password"
                                        value={
                                            confirmPassword
                                        }
                                        onChange={
                                            (event) =>
                                                setConfirmPassword(
                                                    event.target.value
                                                )
                                        }
                                        required
                                    />


                                    <button
                                        type="button"
                                        onClick={() =>
                                            setShowConfirmPassword(
                                                !showConfirmPassword
                                            )
                                        }
                                    >

                                        {
                                            showConfirmPassword
                                                ? "🙈"
                                                : "👁️"
                                        }

                                    </button>

                                </div>

                            </div>


                            {/* Submit */}

                            <button
                                type="submit"
                                className="auth-submit"
                                disabled={loading}
                            >

                                {loading
                                    ? "Resetting Password..."
                                    : "Reset Password →"
                                }

                            </button>


                        </form>


                        {/* Message */}

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


                        {/* Footer */}

                        <div className="auth-footer">

                            <span>

                                Remember your password?

                            </span>


                            <Link to="/login">

                                Back to Login

                            </Link>

                        </div>


                        {/* Security */}

                        <div className="security-note">

                            🔒 Secure & encrypted
                            password recovery

                        </div>


                    </div>

                </div>

            </div>

        </div>
    );
}

export default ResetPassword;