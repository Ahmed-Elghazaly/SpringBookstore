import {useEffect, useState} from 'react';
import {bookApi, publisherApi, categoryApi} from '../api/client';
import toast from 'react-hot-toast';
import {Plus, Pencil, X, Save} from 'lucide-react';

export default function AdminBooks() {
    // State for the list of books displayed in the table
    const [books, setBooks] = useState([]);
    // State for the dropdown options in the create form
    const [publishers, setPublishers] = useState([]);
    const [categories, setCategories] = useState([]);
    // Loading state for the initial data fetch
    const [loading, setLoading] = useState(true);
    // Controls whether the add/edit modal is visible
    const [showModal, setShowModal] = useState(false);
    // If not null, we are editing this book; if null, we are creating a new book
    const [editingBook, setEditingBook] = useState(null);
    
    // Form data state - holds all the values for the add/edit form
    const [formData, setFormData] = useState({
        isbn: '',
        title: '',
        publicationYear: '',
        sellingPrice: '',
        stockQuantity: '',
        thresholdQuantity: '',
        publisherId: '',
        categoryName: '',
        authorNames: ''
    });

    // Load all data when the component mounts
    useEffect(() => {
        loadData();
    }, []);

    // Fetches books, publishers, and categories from the backend
    const loadData = async () => {
        setLoading(true);
        try {
            // Use Promise.all to make all three API calls in parallel for efficiency
            const [booksRes, pubRes, catRes] = await Promise.all([
                bookApi.getAll(),
                publisherApi.getAll(),
                categoryApi.getAll()
            ]);
            setBooks(booksRes.data);
            setPublishers(pubRes.data);
            setCategories(catRes.data);
        } catch (err) {
            console.error(err);
            toast.error('Failed to load data');
        } finally {
            setLoading(false);
        }
    };

    // Resets the form to its initial empty state
    const resetForm = () => {
        setFormData({
            isbn: '',
            title: '',
            publicationYear: '',
            sellingPrice: '',
            stockQuantity: '',
            thresholdQuantity: '',
            publisherId: '',
            categoryName: '',
            authorNames: ''
        });
        setEditingBook(null);
    };

    // Opens the modal in "create" mode with an empty form
    const openAddModal = () => {
        resetForm();
        setShowModal(true);
    };

    // Opens the modal in "edit" mode with the book's current values pre-filled
    const openEditModal = (book) => {
        setEditingBook(book);
        setFormData({
            isbn: book.isbn,
            title: book.title,
            publicationYear: book.publicationYear || '',
            sellingPrice: book.sellingPrice,
            stockQuantity: book.stockQuantity,
            thresholdQuantity: book.thresholdQuantity,
            publisherId: '', // Publisher cannot be changed after creation
            categoryName: book.categoryName,
            authorNames: book.authors ? book.authors.join(', ') : ''
        });
        setShowModal(true);
    };

    // Handles form submission for both create and update operations
    const handleSubmit = async (e) => {
        e.preventDefault();
        
        try {
            if (editingBook) {
                // UPDATE: Only send the fields that can be modified
                // ISBN, publisher, category, and authors cannot be changed after creation
                await bookApi.update(editingBook.isbn, {
                    title: formData.title,
                    sellingPrice: parseFloat(formData.sellingPrice),
                    stockQuantity: parseInt(formData.stockQuantity),
                    thresholdQuantity: parseInt(formData.thresholdQuantity)
                });
                toast.success('Book updated successfully!');
            } else {
                // CREATE: Send all fields including ISBN, publisher, category, authors
                const payload = {
                    isbn: formData.isbn,
                    title: formData.title,
                    publicationYear: formData.publicationYear ? parseInt(formData.publicationYear) : null,
                    sellingPrice: parseFloat(formData.sellingPrice),
                    stockQuantity: parseInt(formData.stockQuantity),
                    thresholdQuantity: parseInt(formData.thresholdQuantity),
                    publisherId: parseInt(formData.publisherId),
                    categoryName: formData.categoryName,
                    // Split comma-separated authors into an array, trim whitespace, and filter empty strings
                    authorNames: formData.authorNames.split(',').map(a => a.trim()).filter(a => a)
                };
                await bookApi.create(payload);
                toast.success('Book created successfully!');
            }
            
            // Close modal, reset form, and reload the book list
            setShowModal(false);
            resetForm();
            loadData();
        } catch (err) {
            console.error(err);
            toast.error(err.response?.data?.message || 'Operation failed');
        }
    };

    // Show loading state while data is being fetched
    if (loading) {
        return (
            <div className="max-w-7xl mx-auto px-4 py-8">
                <div className="text-center py-20 text-gray-500">Loading books...</div>
            </div>
        );
    }

    return (
        <div className="max-w-7xl mx-auto px-4 py-8">
            {/* Page Header with Add Button */}
            <div className="flex justify-between items-center mb-8">
                <h1 className="text-3xl font-bold text-gray-900">Manage Books</h1>
                <button
                    onClick={openAddModal}
                    className="bg-brand-600 text-white px-6 py-3 rounded-lg font-bold hover:bg-brand-700 flex items-center gap-2"
                >
                    <Plus size={20}/> Add New Book
                </button>
            </div>

            {/* Books Table */}
            <div className="bg-white rounded-xl shadow-sm border overflow-hidden">
                <table className="w-full">
                    <thead className="bg-gray-50 border-b">
                        <tr>
                            <th className="p-4 text-left font-semibold text-gray-600">ISBN</th>
                            <th className="p-4 text-left font-semibold text-gray-600">Title</th>
                            <th className="p-4 text-left font-semibold text-gray-600">Category</th>
                            <th className="p-4 text-left font-semibold text-gray-600">Price</th>
                            <th className="p-4 text-left font-semibold text-gray-600">Stock</th>
                            <th className="p-4 text-left font-semibold text-gray-600">Threshold</th>
                            <th className="p-4 text-left font-semibold text-gray-600">Actions</th>
                        </tr>
                    </thead>
                    <tbody className="divide-y">
                        {books.map((book) => (
                            <tr key={book.isbn} className="hover:bg-gray-50">
                                <td className="p-4 font-mono text-sm text-gray-500">{book.isbn}</td>
                                <td className="p-4">
                                    <div className="font-bold text-gray-900">{book.title}</div>
                                    <div className="text-xs text-gray-400">
                                        {book.authors?.join(', ') || 'No authors'}
                                    </div>
                                </td>
                                <td className="p-4 text-gray-600">{book.categoryName}</td>
                                <td className="p-4 font-bold text-green-600">${book.sellingPrice}</td>
                                <td className="p-4">
                                    {/* Show stock with color-coding: red if at/below threshold, green if above */}
                                    <span className={`px-2 py-1 rounded text-sm font-medium ${
                                        book.stockQuantity <= book.thresholdQuantity 
                                            ? 'bg-red-100 text-red-700' 
                                            : 'bg-green-100 text-green-700'
                                    }`}>
                                        {book.stockQuantity}
                                    </span>
                                </td>
                                <td className="p-4 text-gray-500">{book.thresholdQuantity}</td>
                                <td className="p-4">
                                    <button
                                        onClick={() => openEditModal(book)}
                                        className="text-brand-600 hover:bg-brand-50 p-2 rounded-lg"
                                        title="Edit Book"
                                    >
                                        <Pencil size={18}/>
                                    </button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
                
                {/* Empty state when no books exist */}
                {books.length === 0 && (
                    <div className="text-center py-12 text-gray-400">
                        No books found. Add your first book!
                    </div>
                )}
            </div>

            {/* Add/Edit Modal */}
            {showModal && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
                    <div className="bg-white rounded-2xl shadow-2xl w-full max-w-2xl max-h-[90vh] overflow-y-auto">
                        {/* Modal Header */}
                        <div className="flex justify-between items-center p-6 border-b">
                            <h2 className="text-2xl font-bold text-gray-900">
                                {editingBook ? 'Edit Book' : 'Add New Book'}
                            </h2>
                            <button
                                onClick={() => {setShowModal(false); resetForm();}}
                                className="text-gray-400 hover:text-gray-600"
                            >
                                <X size={24}/>
                            </button>
                        </div>

                        {/* Modal Form */}
                        <form onSubmit={handleSubmit} className="p-6 space-y-4">
                            {/* ISBN - disabled when editing because it's the primary key */}
                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1">ISBN *</label>
                                <input
                                    type="text"
                                    value={formData.isbn}
                                    onChange={(e) => setFormData({...formData, isbn: e.target.value})}
                                    disabled={!!editingBook}
                                    className={`w-full p-3 border rounded-lg ${editingBook ? 'bg-gray-100 cursor-not-allowed' : ''}`}
                                    placeholder="978-0-123456-78-9"
                                    required
                                />
                            </div>

                            {/* Title */}
                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1">Title *</label>
                                <input
                                    type="text"
                                    value={formData.title}
                                    onChange={(e) => setFormData({...formData, title: e.target.value})}
                                    className="w-full p-3 border rounded-lg"
                                    placeholder="The Great Book"
                                    required
                                />
                            </div>

                            {/* Price and Year in two columns */}
                            <div className="grid grid-cols-2 gap-4">
                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-1">Price *</label>
                                    <input
                                        type="number"
                                        step="0.01"
                                        min="0"
                                        value={formData.sellingPrice}
                                        onChange={(e) => setFormData({...formData, sellingPrice: e.target.value})}
                                        className="w-full p-3 border rounded-lg"
                                        placeholder="29.99"
                                        required
                                    />
                                </div>
                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-1">Publication Year</label>
                                    <input
                                        type="number"
                                        min="1000"
                                        max="2100"
                                        value={formData.publicationYear}
                                        onChange={(e) => setFormData({...formData, publicationYear: e.target.value})}
                                        className="w-full p-3 border rounded-lg"
                                        placeholder="2024"
                                        disabled={!!editingBook}
                                    />
                                </div>
                            </div>

                            {/* Stock and Threshold in two columns */}
                            <div className="grid grid-cols-2 gap-4">
                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-1">Stock Quantity *</label>
                                    <input
                                        type="number"
                                        min="0"
                                        value={formData.stockQuantity}
                                        onChange={(e) => setFormData({...formData, stockQuantity: e.target.value})}
                                        className="w-full p-3 border rounded-lg"
                                        placeholder="100"
                                        required
                                    />
                                </div>
                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-1">Threshold Quantity *</label>
                                    <input
                                        type="number"
                                        min="0"
                                        value={formData.thresholdQuantity}
                                        onChange={(e) => setFormData({...formData, thresholdQuantity: e.target.value})}
                                        className="w-full p-3 border rounded-lg"
                                        placeholder="10"
                                        required
                                    />
                                </div>
                            </div>

                            {/* Publisher and Category dropdowns - only shown when creating */}
                            {!editingBook && (
                                <div className="grid grid-cols-2 gap-4">
                                    <div>
                                        <label className="block text-sm font-medium text-gray-700 mb-1">Publisher *</label>
                                        <select
                                            value={formData.publisherId}
                                            onChange={(e) => setFormData({...formData, publisherId: e.target.value})}
                                            className="w-full p-3 border rounded-lg"
                                            required
                                        >
                                            <option value="">Select Publisher</option>
                                            {publishers.map((pub) => (
                                                <option key={pub.publisherId} value={pub.publisherId}>
                                                    {pub.name}
                                                </option>
                                            ))}
                                        </select>
                                    </div>
                                    <div>
                                        <label className="block text-sm font-medium text-gray-700 mb-1">Category *</label>
                                        <select
                                            value={formData.categoryName}
                                            onChange={(e) => setFormData({...formData, categoryName: e.target.value})}
                                            className="w-full p-3 border rounded-lg"
                                            required
                                        >
                                            <option value="">Select Category</option>
                                            {categories.map((cat) => (
                                                <option key={cat.categoryName} value={cat.categoryName}>
                                                    {cat.categoryName}
                                                </option>
                                            ))}
                                        </select>
                                    </div>
                                </div>
                            )}

                            {/* Authors input - only shown when creating */}
                            {!editingBook && (
                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-1">Authors (comma-separated)</label>
                                    <input
                                        type="text"
                                        value={formData.authorNames}
                                        onChange={(e) => setFormData({...formData, authorNames: e.target.value})}
                                        className="w-full p-3 border rounded-lg"
                                        placeholder="John Smith, Jane Doe"
                                    />
                                    <p className="text-xs text-gray-400 mt-1">Enter author names separated by commas. New authors will be created automatically.</p>
                                </div>
                            )}

                            {/* Form Actions */}
                            <div className="flex justify-end gap-4 pt-4">
                                <button
                                    type="button"
                                    onClick={() => {setShowModal(false); resetForm();}}
                                    className="px-6 py-3 border rounded-lg font-medium text-gray-600 hover:bg-gray-50"
                                >
                                    Cancel
                                </button>
                                <button
                                    type="submit"
                                    className="px-6 py-3 bg-brand-600 text-white rounded-lg font-bold hover:bg-brand-700 flex items-center gap-2"
                                >
                                    <Save size={18}/> {editingBook ? 'Update Book' : 'Create Book'}
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
}
