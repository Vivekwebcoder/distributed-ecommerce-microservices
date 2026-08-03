import { Link } from "react-router-dom";
import { logoutUser } from "../service/authService";
import {
    isAuthenticated,
    getUserRole
} from "../service/authUtils";

function Navbar() {

    const loggedIn = isAuthenticated();
    const role = getUserRole();

    return (

        <nav>

            <h2>
                E-Cart
            </h2>

            <div>

                <Link to="/">
                    Home
                </Link>

                {" | "}

                {loggedIn ? (

                    <>
                        <span>
                            Role: {role}
                        </span>

                        {" | "}

                        <Link to="/role-test">
                            Dashboard
                        </Link>

                        {" | "}

                        <button
                            onClick={logoutUser}
                        >
                            Logout
                        </button>
                    </>

                ) : (

                    <>
                        <Link to="/login">
                            Login
                        </Link>

                        {" | "}

                        <Link to="/register">
                            Register
                        </Link>
                    </>

                )}

            </div>

        </nav>
    );
}

export default Navbar;