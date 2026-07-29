import { useState, useEffect } from "react";

function ProductForm({ onClose, onSave, product, isEditing }) {
  const [formData, setFormData] = useState({
    name: "",
    brand: "",
    category: "",
    description: "",
    price: "",
    stock: "",
    seller: "",
    imageUrl: "",
    active: true,
  });

  useEffect(() => {
    if (product) {
    setFormData({
        ...product,
        price: product.price ?? "",
        stock: product.stock ?? "",
    });
} else {
    setFormData({
        name: "",
        brand: "",
        category: "",
        description: "",
        price: "",
        stock: "",
        seller: "",
        imageUrl: "",
        active: true,
    });
}
  }, [product]);

  const handleChange = (e) => {
    const { name, value } = e.target;

    setFormData((prev) => ({
      ...prev,
      [name]:
        name === "price" || name === "stock"
          ?value === "" ? "" : Number(value)
          : value,
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    onSave(formData);
  };

  return (
    <div className="fixed inset-0 bg-black/50 flex justify-center items-center z-50">

      <div className="bg-white rounded-2xl shadow-xl w-full max-w-2xl p-8">

        <h2 className="text-3xl font-bold mb-6">
          {isEditing ? "Edit Product" : "Add Product"}
        </h2>

        <form
          onSubmit={handleSubmit}
          className="grid grid-cols-2 gap-4"
        >

          <input
            name="name"
            value={formData.name}
            onChange={handleChange}
            placeholder="Product Name"
            className="border p-3 rounded-lg"
            required
          />

          <input
            name="brand"
            value={formData.brand}
            onChange={handleChange}
            placeholder="Brand"
            className="border p-3 rounded-lg"
            required
          />

          <input
            name="category"
            value={formData.category}
            onChange={handleChange}
            placeholder="Category"
            className="border p-3 rounded-lg"
            required
          />

          <input
            name="price"
            type="number"
            value={formData.price}
            onChange={handleChange}
            placeholder="Price"
            className="border p-3 rounded-lg"
            required
          />

          <input
            name="stock"
            type="number"
            value={formData.stock}
            onChange={handleChange}
            placeholder="Stock"
            className="border p-3 rounded-lg"
            required
          />

          <input
            name="seller"
            value={formData.seller}
            onChange={handleChange}
            placeholder="Seller"
            className="border p-3 rounded-lg"
            required
          />

          <input
            name="imageUrl"
            value={formData.imageUrl}
            onChange={handleChange}
            placeholder="Image URL"
            className="border p-3 rounded-lg col-span-2"
            required
          />

          <textarea
            name="description"
            value={formData.description}
            onChange={handleChange}
            placeholder="Description"
            className="border p-3 rounded-lg col-span-2 h-28 resize-none"
            required
          />

          <div className="col-span-2 flex justify-end gap-3">

            <button
              type="button"
              onClick={onClose}
              className="px-5 py-3 bg-gray-300 rounded-lg hover:bg-gray-400"
            >
              Cancel
            </button>

            <button
              type="submit"
              className="px-5 py-3 bg-emerald-600 text-white rounded-lg hover:bg-emerald-700"
            >
              {isEditing ? "Update Product" : "Save Product"}
            </button>

          </div>

        </form>

      </div>

    </div>
  );
}

export default ProductForm;