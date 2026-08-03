import { Navigate } from "react-router-dom";

function ProtectedRoute({
    children,
    allowedRoles
}) {

    const token =
        localStorage.getItem("token");

    const role =
        localStorage
            .getItem("role")
            ?.toUpperCase();


    // No login
    if (!token) {

        return (
            <Navigate
                to="/login"
                replace
            />
        );
    }


    // Role restriction
    if (
        allowedRoles &&
        !allowedRoles.includes(role)
    ) {

        if (role === "ADMIN") {

            return (
                <Navigate
                    to="/admin-dashboard"
                    replace
                />
            );
        }


        if (role === "SELLER") {

            return (
                <Navigate
                    to="/seller-dashboard"
                    replace
                />
            );
        }


        return (
            <Navigate
                to="/customer-dashboard"
                replace
            />
        );
    }


    return children;
}

export default ProtectedRoute;