import { useEffect, useState } from "react";
import { FaPlus, FaSearch, FaBoxOpen } from "react-icons/fa";
import API from "./services/api";
import ProductCard from "./components/ProductCard";
import ProductForm from "./components/ProductForm";

function App() {
  const [products, setProducts] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [showForm, setShowForm] = useState(false);
  const [selectedProduct, setSelectedProduct] = useState(null);
  const [isEditing, setIsEditing] = useState(false);

  useEffect(() => {
    fetchProducts();
  }, []);

  const fetchProducts = async () => {
    try {
      const response = await API.get("/products");
      setProducts(response.data);
    } catch (error) {
      console.error(error);
    }
  };

const handleSaveProduct = async (product) => {
  try {
    if (isEditing) {
      await API.put(`/products/${selectedProduct.id}`, product);
    } else {
      await API.post("/products", product);
    }

    setShowForm(false);
    setSelectedProduct(null);
    setIsEditing(false);

    fetchProducts();
  } catch (error) {
    console.error(error);
    alert("Operation failed.");
  }
};
const handleDeleteProduct = async (id) => {
  const confirmDelete = window.confirm(
    "Are you sure you want to delete this product?"
  );

  if (!confirmDelete) return;

  try {
    await API.delete(`/products/${id}`);
    fetchProducts();
  } catch (error) {
    console.error(error);
    alert("Failed to delete product.");
  }
};
const handleEditProduct = (product) => {
  setSelectedProduct(product);
  setIsEditing(true);
  setShowForm(true);
};

  const filteredProducts = products.filter((product) =>
    product.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
    product.brand.toLowerCase().includes(searchTerm.toLowerCase()) ||
    product.category.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-50 via-gray-100 to-emerald-50">

      {/* ===================== NAVBAR ===================== */}
      <header className="bg-white shadow-md border-b border-gray-200 sticky top-0 z-50">

        <div className="max-w-7xl mx-auto px-6 py-4">

          <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-5">

            {/* Title */}
            <div>
              <h1 className="text-4xl font-bold text-slate-800">
                🛍 Product Management
              </h1>

              <p className="text-gray-500 mt-1">
                Manage your inventory with ease
              </p>
            </div>

            {/* Search + Button */}
            <div className="flex flex-col sm:flex-row items-center gap-4 w-full md:w-auto">

              <div className="relative w-full sm:w-96">

                <FaSearch className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400" />

                <input
                  type="text"
                  placeholder="Search products..."
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="
                    w-full
                    pl-11
                    pr-4
                    py-3
                    rounded-xl
                    bg-gray-100
                    border
                    border-gray-200
                    focus:outline-none
                    focus:ring-2
                    focus:ring-emerald-500
                  "
                />

              </div>

              <button
                onClick={() => setShowForm(true)}
                className="
                  flex
                  items-center
                  gap-2
                  bg-emerald-600
                  hover:bg-emerald-700
                  text-white
                  px-6
                  py-3
                  rounded-xl
                  font-semibold
                  transition
                "
              >
                <FaPlus />
                Add Product
              </button>

            </div>

          </div>

        </div>

      </header>

      {/* ===================== BODY ===================== */}

      <div className="max-w-7xl mx-auto px-6 py-8">

        {/* Dashboard Card */}

        <div className="bg-white rounded-2xl shadow-sm border border-gray-200 p-6 w-72">

          <div className="flex items-center gap-4">

            <div className="bg-emerald-100 p-4 rounded-xl">
              <FaBoxOpen className="text-emerald-700 text-2xl" />
            </div>

            <div>

              <p className="text-gray-500">
                Total Products
              </p>

              <h2 className="text-4xl font-bold text-slate-800">
                {filteredProducts.length}
              </h2>

            </div>

          </div>

        </div>

        {/* Product Grid */}

        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-3 gap-8 mt-8">

          {filteredProducts.length > 0 ? (

            filteredProducts.map((product) => (
              <ProductCard
  key={product.id}
  product={product}
  onDelete={handleDeleteProduct}
  onEdit={handleEditProduct}
/>
            ))

          ) : (

            <div className="col-span-full bg-white rounded-2xl shadow-sm p-16 text-center">

              <h2 className="text-2xl font-bold text-gray-600">
                No Products Found
              </h2>

              <p className="text-gray-500 mt-2">
                Try another search keyword.
              </p>

            </div>

          )}

        </div>

      </div>

      {/* Product Form Modal */}

      {showForm && (
  <ProductForm
    onClose={() => {
      setShowForm(false);
      setSelectedProduct(null);
      setIsEditing(false);
    }}
    onSave={handleSaveProduct}
    product={selectedProduct}
    isEditing={isEditing}
  />
)}

    </div>
  );
}

export default App;