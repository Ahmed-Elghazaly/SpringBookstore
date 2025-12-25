import {useEffect, useState} from 'react';
import api from '../api/client';
import toast from 'react-hot-toast';
import {Building2, Plus, RefreshCw, Save, Tag, X} from 'lucide-react';

export default function AdminSettings() {
    // Data states
    const [publishers, setPublishers] = useState([]);
    const [categories, setCategories] = useState([]);
    const [loading, setLoading] = useState(true);
    
    // Modal states
    const [showPublisherModal, setShowPublisherModal] = useState(false);
    const [showCategoryModal, setShowCategoryModal] = useState(false);
    
    // Form states
    const [publisherForm, setPublisherForm] = useState({ name: '', address: '', phoneNumber: '' });
    const [categoryForm, setCategoryForm] = useState({ categoryName: '' });
    const [submitting, setSubmitting] = useState(false);

    useEffect(() => {
        loadData();
    }, []);

    const loadData = async () => {
        setLoading(true);
        try {
            const [pubRes, catRes] = await Promise.all([
                api.get('/publishers'),
                api.get('/categories')
            ]);
            setPublishers(pubRes.data);
            setCategories(catRes.data);
        } catch (err) {
            console.error(err);
            toast.error('Failed to load data');
        } finally {
            setLoading(false);
        }
    };

    // Publisher handlers
    const handleAddPublisher = async (e) => {
        e.preventDefault();
        setSubmitting(true);
        try {
            await api.post('/publishers', publisherForm);
            toast.success('Publisher added successfully!');
            setShowPublisherModal(false);
            setPublisherForm({ name: '', address: '', phoneNumber: '' });
            loadData();
        } catch (err) {
            console.error(err);
            toast.error(err.response?.data?.message || 'Failed to add publisher');
        } finally {
            setSubmitting(false);
        }
    };

    // Category handlers
    const handleAddCategory = async (e) => {
        e.preventDefault();
        setSubmitting(true);
        try {
            await api.post('/categories', categoryForm);
            toast.success('Category added successfully!');
            setShowCategoryModal(false);
            setCategoryForm({ categoryName: '' });
            loadData();
        } catch (err) {
            console.error(err);
            toast.error(err.response?.data?.message || 'Failed to add category');
        } finally {
            setSubmitting(false);
        }
    };

    if (loading) {
        return (
            <div className="max-w-6xl mx-auto px-4 py-8">
                <div className="text-center py-20 text-gray-500">Loading settings...</div>
            </div>
        );
    }

    return (
        <div className="max-w-6xl mx-auto px-4 py-8">
            {/* Page Header */}
            <div className="flex justify-between items-center mb-8">
                <div>
                    <h1 className="text-3xl font-bold text-gray-900">Admin Settings</h1>
                    <p className="text-gray-500 mt-1">Manage publishers and categories</p>
                </div>
                <button
                    onClick={loadData}
                    className="bg-gray-100 text-gray-600 px-4 py-2 rounded-lg font-medium hover:bg-gray-200 flex items-center gap-2"
                >
                    <RefreshCw size={18}/> Refresh
                </button>
            </div>

            <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
                {/* Publishers Section */}
                <div className="bg-white rounded-xl shadow-sm border overflow-hidden">
                    <div className="bg-gray-50 p-4 border-b flex justify-between items-center">
                        <div className="flex items-center gap-2">
                            <Building2 size={20} className="text-green-600"/>
                            <h2 className="text-lg font-bold text-gray-900">Publishers</h2>
                            <span className="bg-green-100 text-green-700 px-2 py-0.5 rounded text-sm">
                                {publishers.length}
                            </span>
                        </div>
                        <button
                            onClick={() => setShowPublisherModal(true)}
                            className="bg-green-600 text-white px-4 py-2 rounded-lg font-medium hover:bg-green-700 flex items-center gap-2 text-sm"
                        >
                            <Plus size={16}/> Add
                        </button>
                    </div>
                    <div className="max-h-96 overflow-y-auto">
                        {publishers.length === 0 ? (
                            <div className="text-center py-8 text-gray-400">
                                No publishers yet. Add your first publisher!
                            </div>
                        ) : (
                            <ul className="divide-y">
                                {publishers.map((pub) => (
                                    <li key={pub.publisherId} className="p-4 hover:bg-gray-50">
                                        <div className="font-bold text-gray-900">{pub.name}</div>
                                        {pub.address && (
                                            <div className="text-sm text-gray-500 mt-1">{pub.address}</div>
                                        )}
                                        {pub.phoneNumber && (
                                            <div className="text-sm text-gray-400">{pub.phoneNumber}</div>
                                        )}
                                    </li>
                                ))}
                            </ul>
                        )}
                    </div>
                </div>

                {/* Categories Section */}
                <div className="bg-white rounded-xl shadow-sm border overflow-hidden">
                    <div className="bg-gray-50 p-4 border-b flex justify-between items-center">
                        <div className="flex items-center gap-2">
                            <Tag size={20} className="text-purple-600"/>
                            <h2 className="text-lg font-bold text-gray-900">Categories</h2>
                            <span className="bg-purple-100 text-purple-700 px-2 py-0.5 rounded text-sm">
                                {categories.length}
                            </span>
                        </div>
                        <button
                            onClick={() => setShowCategoryModal(true)}
                            className="bg-purple-600 text-white px-4 py-2 rounded-lg font-medium hover:bg-purple-700 flex items-center gap-2 text-sm"
                        >
                            <Plus size={16}/> Add
                        </button>
                    </div>
                    <div className="max-h-96 overflow-y-auto">
                        {categories.length === 0 ? (
                            <div className="text-center py-8 text-gray-400">
                                No categories yet. Add your first category!
                            </div>
                        ) : (
                            <div className="p-4 flex flex-wrap gap-2">
                                {categories.map((cat) => (
                                    <span 
                                        key={cat.categoryName} 
                                        className="px-3 py-2 bg-purple-100 text-purple-700 rounded-lg text-sm font-medium"
                                    >
                                        {cat.categoryName}
                                    </span>
                                ))}
                            </div>
                        )}
                    </div>
                </div>
            </div>

            {/* Add Publisher Modal */}
            {showPublisherModal && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
                    <div className="bg-white rounded-2xl shadow-2xl w-full max-w-md">
                        <div className="flex justify-between items-center p-6 border-b">
                            <h2 className="text-xl font-bold text-gray-900 flex items-center gap-2">
                                <Building2 size={24} className="text-green-600"/> Add Publisher
                            </h2>
                            <button
                                onClick={() => {setShowPublisherModal(false); setPublisherForm({ name: '', address: '', phoneNumber: '' });}}
                                className="text-gray-400 hover:text-gray-600"
                            >
                                <X size={24}/>
                            </button>
                        </div>
                        <form onSubmit={handleAddPublisher} className="p-6 space-y-4">
                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1">
                                    Name <span className="text-red-500">*</span>
                                </label>
                                <input
                                    type="text"
                                    value={publisherForm.name}
                                    onChange={(e) => setPublisherForm({...publisherForm, name: e.target.value})}
                                    className="w-full p-3 border rounded-lg focus:ring-2 focus:ring-green-500 outline-none"
                                    placeholder="Penguin Random House"
                                    required
                                />
                            </div>
                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1">
                                    Address
                                </label>
                                <input
                                    type="text"
                                    value={publisherForm.address}
                                    onChange={(e) => setPublisherForm({...publisherForm, address: e.target.value})}
                                    className="w-full p-3 border rounded-lg focus:ring-2 focus:ring-green-500 outline-none"
                                    placeholder="123 Publisher St, New York, NY"
                                />
                            </div>
                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1">
                                    Phone Number
                                </label>
                                <input
                                    type="text"
                                    value={publisherForm.phoneNumber}
                                    onChange={(e) => setPublisherForm({...publisherForm, phoneNumber: e.target.value})}
                                    className="w-full p-3 border rounded-lg focus:ring-2 focus:ring-green-500 outline-none"
                                    placeholder="555-123-4567"
                                />
                            </div>
                            <div className="flex justify-end gap-4 pt-4">
                                <button
                                    type="button"
                                    onClick={() => {setShowPublisherModal(false); setPublisherForm({ name: '', address: '', phoneNumber: '' });}}
                                    className="px-6 py-3 border rounded-lg font-medium text-gray-600 hover:bg-gray-50"
                                    disabled={submitting}
                                >
                                    Cancel
                                </button>
                                <button
                                    type="submit"
                                    disabled={submitting}
                                    className="px-6 py-3 bg-green-600 text-white rounded-lg font-bold hover:bg-green-700 flex items-center gap-2 disabled:bg-gray-400"
                                >
                                    {submitting ? (
                                        <><RefreshCw size={18} className="animate-spin"/> Saving...</>
                                    ) : (
                                        <><Save size={18}/> Add Publisher</>
                                    )}
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}

            {/* Add Category Modal */}
            {showCategoryModal && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
                    <div className="bg-white rounded-2xl shadow-2xl w-full max-w-md">
                        <div className="flex justify-between items-center p-6 border-b">
                            <h2 className="text-xl font-bold text-gray-900 flex items-center gap-2">
                                <Tag size={24} className="text-purple-600"/> Add Category
                            </h2>
                            <button
                                onClick={() => {setShowCategoryModal(false); setCategoryForm({ categoryName: '' });}}
                                className="text-gray-400 hover:text-gray-600"
                            >
                                <X size={24}/>
                            </button>
                        </div>
                        <form onSubmit={handleAddCategory} className="p-6 space-y-4">
                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1">
                                    Category Name <span className="text-red-500">*</span>
                                </label>
                                <input
                                    type="text"
                                    value={categoryForm.categoryName}
                                    onChange={(e) => setCategoryForm({categoryName: e.target.value})}
                                    className="w-full p-3 border rounded-lg focus:ring-2 focus:ring-purple-500 outline-none"
                                    placeholder="Science Fiction"
                                    required
                                />
                            </div>
                            <div className="flex justify-end gap-4 pt-4">
                                <button
                                    type="button"
                                    onClick={() => {setShowCategoryModal(false); setCategoryForm({ categoryName: '' });}}
                                    className="px-6 py-3 border rounded-lg font-medium text-gray-600 hover:bg-gray-50"
                                    disabled={submitting}
                                >
                                    Cancel
                                </button>
                                <button
                                    type="submit"
                                    disabled={submitting}
                                    className="px-6 py-3 bg-purple-600 text-white rounded-lg font-bold hover:bg-purple-700 flex items-center gap-2 disabled:bg-gray-400"
                                >
                                    {submitting ? (
                                        <><RefreshCw size={18} className="animate-spin"/> Saving...</>
                                    ) : (
                                        <><Save size={18}/> Add Category</>
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
