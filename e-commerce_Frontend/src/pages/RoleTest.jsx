import { useState } from "react";

import {
    getAdminData,
    getSellerData,
    getCustomerData,
    getAdminSellerData,
} from "../service/roleService";

function RoleTest() {

    const [message, setMessage] = useState("");

    const [loading, setLoading] = useState(false);

    const callApi = async (apiFunction) => {

        setMessage("");
        setLoading(true);

        try {

            const response = await apiFunction();

            setMessage(response);

        } catch (error) {

            console.error(
                "API Error:",
                error
            );

            if (error.response) {

                setMessage(
                    error.response.data.message ||
                    "Access Denied"
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

        <div style={{ padding: "30px" }}>

            <h1>
                Role Authorization Test
            </h1>

            <p>
                Current Role:{" "}
                {localStorage.getItem("role")}
            </p>

            <hr />

            <button
                onClick={() =>
                    callApi(getAdminData)
                }
            >
                Test Admin API
            </button>

            <br />
            <br />

            <button
                onClick={() =>
                    callApi(getSellerData)
                }
            >
                Test Seller API
            </button>

            <br />
            <br />

            <button
                onClick={() =>
                    callApi(getCustomerData)
                }
            >
                Test Customer API
            </button>

            <br />
            <br />

            <button
                onClick={() =>
                    callApi(getAdminSellerData)
                }
            >
                Test Admin + Seller API
            </button>

            <br />
            <br />

            {loading && (
                <p>
                    Loading...
                </p>
            )}

            {message && (
                <h3>
                    {message}
                </h3>
            )}

        </div>
    );
}

export default RoleTest;