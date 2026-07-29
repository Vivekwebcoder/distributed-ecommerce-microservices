import {
  FaEdit,
  FaTrash,
  FaBoxOpen,
  FaTag,
  FaRupeeSign,
} from "react-icons/fa";

function ProductCard({ product, onDelete, onEdit }) {  return (
    <div
      className="
        bg-white
        rounded-3xl
        overflow-hidden
        shadow-md
        hover:shadow-2xl
        hover:-translate-y-2
        transition-all
        duration-300
        border
        border-gray-200
      "
    >
      {/* Product Image */}
<div className="bg-gradient-to-br from-gray-50 to-gray-100 h-80 flex items-center justify-center p-8">        <img
          src={product.imageUrl}
          alt={product.name}
className="max-h-[90%] object-contain transition duration-500 hover:scale-110"        />
      </div>

      {/* Product Details */}
      <div className="p-6">

        {/* Product Name */}
        <h2 className="text-xl font-bold text-slate-800 line-clamp-1">
          {product.name}
        </h2>

        {/* Description */}
        <p className="text-gray-500 text-sm mt-2 h-10 overflow-hidden">
          {product.description}
        </p>

        {/* Brand */}
        <div className="flex items-center justify-between mt-5">
          <div className="flex items-center gap-2 text-gray-500">
            <FaTag />
            <span>Brand</span>
          </div>

          <span className="font-semibold text-slate-700">
            {product.brand}
          </span>
        </div>

        {/* Category */}
        <div className="flex items-center justify-between mt-3">
          <div className="flex items-center gap-2 text-gray-500">
            <FaBoxOpen />
            <span>Category</span>
          </div>

          <span
            className="
              bg-blue-100
              text-blue-700
              px-3
              py-1
              rounded-full
              text-sm
              font-medium
            "
          >
            {product.category}
          </span>
        </div>

        {/* Price */}
        <div className="flex items-center justify-between mt-4">
          <span className="text-gray-500 flex items-center gap-2">
            <FaRupeeSign />
            Price
          </span>

          <span className="text-2xl font-bold text-emerald-600">
            ₹{product.price}
          </span>
        </div>

        {/* Stock */}
        <div className="flex items-center justify-between mt-4">
          <span className="text-gray-500">
            Stock
          </span>

          <span
            className={`
              px-4 py-1 rounded-full text-sm font-semibold
              ${
                product.stock > 10
                  ? "bg-green-100 text-green-700"
                  : product.stock > 0
                  ? "bg-yellow-100 text-yellow-700"
                  : "bg-red-100 text-red-700"
              }
            `}
          >
            {product.stock} Available
          </span>
        </div>

        {/* Divider */}
        <hr className="my-6" />

        {/* Buttons */}
        <div className="flex gap-3">

         <button
  onClick={() => onEdit(product)}
  className="
    flex-1
    flex
    justify-center
    items-center
    gap-2
    bg-amber-500
    hover:bg-amber-600
    text-white
    py-3
    rounded-xl
    transition
    font-semibold
  "
>
  <FaEdit />
  Edit
</button>

         <button
  onClick={() => onDelete(product.id)}
  className="
    flex-1
    flex
    justify-center
    items-center
    gap-2
    bg-red-600
    hover:bg-red-700
    text-white
    py-3
    rounded-xl
    transition
    font-semibold
  "
>
  <FaTrash />
  Delete
</button>

        </div>

      </div>
    </div>
  );
}

export default ProductCard;