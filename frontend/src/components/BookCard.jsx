import {Plus} from 'lucide-react';
import toast from 'react-hot-toast';
import {cartApi} from '../api/client';

export default function BookCard({book}) {
    const customerId = localStorage.getItem('customerId');

    const handleAddToCart = async () => {
        if (!customerId) return toast.error("Please login first");
        try {
            await cartApi.add(customerId, {isbn: book.isbn, quantity: 1});
            toast.success("Added to cart!");
        } catch (err) {
            toast.error("Failed to add book");
        }
    };

    return (<div
            className="bg-white rounded-xl shadow-sm border border-gray-100 hover:shadow-md transition-all duration-200 overflow-hidden flex flex-col h-full">
            <div className="h-48 bg-brand-50 flex items-center justify-center text-brand-200">
                {/* Placeholder for Book Cover */}
                <span className="text-6xl font-black opacity-20">BOOK</span>
            </div>

            <div className="p-5 flex-1 flex flex-col">
                <div className="flex-1">
                    <p className="text-xs font-bold text-brand-600 uppercase tracking-wide mb-1">
                        {book.categoryName}
                    </p>
                    <h3 className="text-lg font-bold text-gray-900 leading-tight mb-2 line-clamp-2">
                        {book.title}
                    </h3>
                    <p className="text-sm text-gray-500 mb-1">
                        by {book.authors && book.authors.length > 0 ? book.authors.join(', ') : 'Unknown'}
                    </p>
                    <p className="text-xs text-gray-400 mb-1">{book.publisherName}</p>
                    <p className="text-xs text-gray-400 mb-2">
                        {book.publicationYear || 'Year unknown'} • ISBN: {book.isbn}
                    </p>
                    <p className="text-xs font-semibold text-green-600">
                        {book.stockQuantity > 0 ? `${book.stockQuantity} in stock` : 'Out of Stock'}
                    </p>
                </div>

                <div className="flex items-center justify-between mt-4 pt-4 border-t border-gray-100">
                    <span className="text-xl font-bold text-gray-900">${book.sellingPrice.toFixed(2)}</span>
                    <button
                        onClick={handleAddToCart}
                        disabled={book.stockQuantity < 1}
                        className="flex items-center gap-2 bg-brand-600 text-white px-4 py-2 rounded-lg hover:bg-brand-700 disabled:bg-gray-300 disabled:cursor-not-allowed transition-colors"
                    >
                        <Plus size={18}/> Add
                    </button>
                </div>
            </div>
        </div>);
}