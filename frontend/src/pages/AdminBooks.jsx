import {useEffect, useState} from 'react';
import {bookApi, categoryApi, publisherApi} from '../api/client';
import toast from 'react-hot-toast';
import {Pencil, Plus, RefreshCw, Save, X} from 'lucide-react';

export default function AdminBooks() {
    const [books, setBooks] = useState([]);
    const [publishers, setPublishers] = useState([]);
    const [categories, setCategories] = useState([]);
    const [loading, setLoading] = useState(true);
    const [showModal, setShowModal] = useState(false);
    const [editingBook, setEditingBook] = useState(null);
    const [submitting, setSubmitting] = useState(false);
    
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

    useEffect(() => {
        loadData();
    }, []);

    const loadData = async () => {
        setLoading(true);
        try {
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

    const openAddModal = () => {
        resetForm();
        setShowModal(true);
    };

    const openEditModal = (book) => {
        setEditingBook(book);
        setFormData({
            isbn: book.isbn,
            title: book.title,
            publicationYear: book.publicationYear || '',
            sellingPrice: book.sellingPrice,
            stockQuantity: book.stockQuantity,
            thresholdQuantity: book.thresholdQuantity,
            publisherId: '',
            categoryName: book.categoryName,
            authorNames: book.authors ? book.authors.join(', ') : ''
        });
        setShowModal(true);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setSubmitting(true);
        
        try {
            if (editingBook) {
                // UPDATE existing book
                await bookApi.update(editingBook.isbn, {
                    title: formData.title,
                    sellingPrice: parseFloat(formData.sellingPrice),
                    stockQuantity: parseInt(formData.stockQuantity),
                    thresholdQuantity: parseInt(formData.thresholdQuantity)
                });
                toast.success('Book updated successfully!');
            } else {
                // CREATE new book
                // Parse authors - split by comma, trim whitespace, filter empty strings
                const authorList = formData.authorNames
                    ? formData.authorNames.split(',').map(a => a.trim()).filter(a => a.length > 0)
                    : [];
                
                const payload = {
                    isbn: formData.isbn.trim(),
                    title: formData.title.trim(),
                    publicationYear: formData.publicationYear ? parseInt(formData.publicationYear) : null,
                    sellingPrice: parseFloat(formData.sellingPrice),
                    stockQuantity: parseInt(formData.stockQuantity),
                    thresholdQuantity: parseInt(formData.thresholdQuantity),
                    publisherId: parseInt(formData.publisherId),
                    categoryName: formData.categoryName,
                    authorNames: authorList  // This can be empty array - that's OK
                };
                
                console.log('Creating book with payload:', payload);
                await bookApi.create(payload);
                toast.success('Book created successfully!');
            }
            
            setShowModal(false);
            resetForm();
            loadData();
        } catch (err) {
            console.error('Error:', err);
            const errorMsg = err.response?.data?.message || err.response?.data?.error || 'Operation failed';
            toast.error(errorMsg);
        } finally {
            setSubmitting(false);
        }
    };

    if (loading) {
        return (
            <div className="max-w-7xl mx-auto px-4 py-8">
                <div className="text-center py-20 text-gray-500">Loading books...</div>
            </div>
        );
    }

    return (
        <div className="max-w-7xl mx-auto px-4 py-8">
            {/* Page Header */}
            <div className="flex justify-between items-center mb-8">
                <div>
                    <h1 className="text-3xl font-bold text-gray-900">Manage Books</h1>
                    <p className="text-gray-500 mt-1">{books.length} books in catalog</p>
                </div>
                <div className="flex gap-3">
                    <button
                        onClick={loadData}
                        className="bg-gray-100 text-gray-600 px-4 py-3 rounded-lg font-medium hover:bg-gray-200 flex items-center gap-2"
                    >
                        <RefreshCw size={18}/> Refresh
                    </button>
                    <button
                        onClick={openAddModal}
                        className="bg-brand-600 text-white px-6 py-3 rounded-lg font-bold hover:bg-brand-700 flex items-center gap-2"
                    >
                        <Plus size={20}/> Add New Book
                    </button>
                </div>
            </div>

            {/* Info Cards */}
            <div className="grid grid-cols-3 gap-4 mb-8">
                <div className="bg-white p-4 rounded-xl shadow-sm border">
                    <div className="text-sm text-gray-500">Total Books</div>
                    <div className="text-2xl font-bold text-brand-600">{books.length}</div>
                </div>
                <div className="bg-white p-4 rounded-xl shadow-sm border">
                    <div className="text-sm text-gray-500">Publishers</div>
                    <div className="text-2xl font-bold text-green-600">{publishers.length}</div>
                </div>
                <div className="bg-white p-4 rounded-xl shadow-sm border">
                    <div className="text-sm text-gray-500">Categories</div>
                    <div className="text-2xl font-bold text-purple-600">{categories.length}</div>
                </div>
            </div>

            {/* Books Table */}
            <div className="bg-white rounded-xl shadow-sm border overflow-hidden">
                <div className="overflow-x-auto">
                    <table className="w-full">
                        <thead className="bg-gray-50 border-b">
                            <tr>
                                <th className="p-4 text-left font-semibold text-gray-600">ISBN</th>
                                <th className="p-4 text-left font-semibold text-gray-600">Title</th>
                                <th className="p-4 text-left font-semibold text-gray-600">Category</th>
                                <th className="p-4 text-left font-semibold text-gray-600">Publisher</th>
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
                                            {book.authors?.length > 0 ? book.authors.join(', ') : 'No authors'}
                                        </div>
                                    </td>
                                    <td className="p-4">
                                        <span className="px-2 py-1 bg-purple-100 text-purple-700 rounded text-sm">
                                            {book.categoryName}
                                        </span>
                                    </td>
                                    <td className="p-4 text-gray-600 text-sm">{book.publisherName}</td>
                                    <td className="p-4 font-bold text-green-600">${book.sellingPrice?.toFixed(2)}</td>
                                    <td className="p-4">
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
                </div>
                
                {books.length === 0 && (
                    <div className="text-center py-12 text-gray-400">
                        No books found. Add your first book!
                    </div>
                )}
            </div>

            {/* Add/Edit Modal */}
            {showModal && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
                    <div className="bg-white rounded-2xl shadow-2xl w-full max-w-2xl max-h-[90vh] overflow-y-auto">
                        {/* Modal Header */}
                        <div className="flex justify-between items-center p-6 border-b sticky top-0 bg-white">
                            <h2 className="text-2xl font-bold text-gray-900">
                                {editingBook ? 'Edit Book' : 'Add New Book'}
                            </h2>
                            <button
                                onClick={() => {setShowModal(false); resetForm();}}
                                className="text-gray-400 hover:text-gray-600 p-2"
                            >
                                <X size={24}/>
                            </button>
                        </div>

                        {/* Modal Form */}
                        <form onSubmit={handleSubmit} className="p-6 space-y-4">
                            {/* ISBN */}
                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1">
                                    ISBN <span className="text-red-500">*</span>
                                </label>
                                <input
                                    type="text"
                                    value={formData.isbn}
                                    onChange={(e) => setFormData({...formData, isbn: e.target.value})}
                                    disabled={!!editingBook}
                                    className={`w-full p-3 border rounded-lg focus:ring-2 focus:ring-brand-500 outline-none ${editingBook ? 'bg-gray-100 cursor-not-allowed' : ''}`}
                                    placeholder="978-0-123456-78-9"
                                    required
                                />
                                {editingBook && <p className="text-xs text-gray-400 mt-1">ISBN cannot be changed</p>}
                            </div>

                            {/* Title */}
                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1">
                                    Title <span className="text-red-500">*</span>
                                </label>
                                <input
                                    type="text"
                                    value={formData.title}
                                    onChange={(e) => setFormData({...formData, title: e.target.value})}
                                    className="w-full p-3 border rounded-lg focus:ring-2 focus:ring-brand-500 outline-none"
                                    placeholder="The Great Book"
                                    required
                                />
                            </div>

                            {/* Price and Year */}
                            <div className="grid grid-cols-2 gap-4">
                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-1">
                                        Price ($) <span className="text-red-500">*</span>
                                    </label>
                                    <input
                                        type="number"
                                        step="0.01"
                                        min="0"
                                        value={formData.sellingPrice}
                                        onChange={(e) => setFormData({...formData, sellingPrice: e.target.value})}
                                        className="w-full p-3 border rounded-lg focus:ring-2 focus:ring-brand-500 outline-none"
                                        placeholder="29.99"
                                        required
                                    />
                                </div>
                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-1">
                                        Publication Year
                                    </label>
                                    <input
                                        type="number"
                                        min="1000"
                                        max="2100"
                                        value={formData.publicationYear}
                                        onChange={(e) => setFormData({...formData, publicationYear: e.target.value})}
                                        className={`w-full p-3 border rounded-lg focus:ring-2 focus:ring-brand-500 outline-none ${editingBook ? 'bg-gray-100 cursor-not-allowed' : ''}`}
                                        placeholder="2024"
                                        disabled={!!editingBook}
                                    />
                                </div>
                            </div>

                            {/* Stock and Threshold */}
                            <div className="grid grid-cols-2 gap-4">
                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-1">
                                        Stock Quantity <span className="text-red-500">*</span>
                                    </label>
                                    <input
                                        type="number"
                                        min="0"
                                        value={formData.stockQuantity}
                                        onChange={(e) => setFormData({...formData, stockQuantity: e.target.value})}
                                        className="w-full p-3 border rounded-lg focus:ring-2 focus:ring-brand-500 outline-none"
                                        placeholder="100"
                                        required
                                    />
                                </div>
                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-1">
                                        Threshold <span className="text-red-500">*</span>
                                        <span className="text-xs text-gray-400 ml-1">(auto-order trigger)</span>
                                    </label>
                                    <input
                                        type="number"
                                        min="0"
                                        value={formData.thresholdQuantity}
                                        onChange={(e) => setFormData({...formData, thresholdQuantity: e.target.value})}
                                        className="w-full p-3 border rounded-lg focus:ring-2 focus:ring-brand-500 outline-none"
                                        placeholder="10"
                                        required
                                    />
                                </div>
                            </div>

                            {/* Publisher and Category - only when creating */}
                            {!editingBook && (
                                <div className="grid grid-cols-2 gap-4">
                                    <div>
                                        <label className="block text-sm font-medium text-gray-700 mb-1">
                                            Publisher <span className="text-red-500">*</span>
                                        </label>
                                        <select
                                            value={formData.publisherId}
                                            onChange={(e) => setFormData({...formData, publisherId: e.target.value})}
                                            className="w-full p-3 border rounded-lg focus:ring-2 focus:ring-brand-500 outline-none"
                                            required
                                        >
                                            <option value="">Select Publisher</option>
                                            {publishers.map((pub) => (
                                                <option key={pub.publisherId} value={pub.publisherId}>
                                                    {pub.name}
                                                </option>
                                            ))}
                                        </select>
                                        {publishers.length === 0 && (
                                            <p className="text-xs text-red-500 mt-1">No publishers available. Add publishers first.</p>
                                        )}
                                    </div>
                                    <div>
                                        <label className="block text-sm font-medium text-gray-700 mb-1">
                                            Category <span className="text-red-500">*</span>
                                        </label>
                                        <select
                                            value={formData.categoryName}
                                            onChange={(e) => setFormData({...formData, categoryName: e.target.value})}
                                            className="w-full p-3 border rounded-lg focus:ring-2 focus:ring-brand-500 outline-none"
                                            required
                                        >
                                            <option value="">Select Category</option>
                                            {categories.map((cat) => (
                                                <option key={cat.categoryName} value={cat.categoryName}>
                                                    {cat.categoryName}
                                                </option>
                                            ))}
                                        </select>
                                        {categories.length === 0 && (
                                            <p className="text-xs text-red-500 mt-1">No categories available. Add categories first.</p>
                                        )}
                                    </div>
                                </div>
                            )}

                            {/* Authors - only when creating, NOT REQUIRED */}
                            {!editingBook && (
                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-1">
                                        Authors <span className="text-xs text-gray-400">(optional, comma-separated)</span>
                                    </label>
                                    <input
                                        type="text"
                                        value={formData.authorNames}
                                        onChange={(e) => setFormData({...formData, authorNames: e.target.value})}
                                        className="w-full p-3 border rounded-lg focus:ring-2 focus:ring-brand-500 outline-none"
                                        placeholder="John Smith, Jane Doe"
                                    />
                                    <p className="text-xs text-gray-400 mt-1">
                                        Enter author names separated by commas. New authors will be created automatically.
                                    </p>
                                </div>
                            )}

                            {/* Showing current info when editing */}
                            {editingBook && (
                                <div className="bg-gray-50 p-4 rounded-lg">
                                    <p className="text-sm text-gray-500 mb-2">Current book info (cannot be changed):</p>
                                    <p className="text-sm"><strong>Publisher:</strong> {editingBook.publisherName}</p>
                                    <p className="text-sm"><strong>Category:</strong> {editingBook.categoryName}</p>
                                    <p className="text-sm"><strong>Authors:</strong> {editingBook.authors?.join(', ') || 'None'}</p>
                                </div>
                            )}

                            {/* Form Actions */}
                            <div className="flex justify-end gap-4 pt-4 border-t">
                                <button
                                    type="button"
                                    onClick={() => {setShowModal(false); resetForm();}}
                                    className="px-6 py-3 border rounded-lg font-medium text-gray-600 hover:bg-gray-50"
                                    disabled={submitting}
                                >
                                    Cancel
                                </button>
                                <button
                                    type="submit"
                                    disabled={submitting}
                                    className="px-6 py-3 bg-brand-600 text-white rounded-lg font-bold hover:bg-brand-700 flex items-center gap-2 disabled:bg-gray-400 disabled:cursor-not-allowed"
                                >
                                    {submitting ? (
                                        <>
                                            <RefreshCw size={18} className="animate-spin"/> Saving...
                                        </>
                                    ) : (
                                        <>
                                            <Save size={18}/> {editingBook ? 'Update Book' : 'Create Book'}
                                        </>
                                    )}
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
}
