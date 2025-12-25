import {useEffect, useState} from 'react';
import {bookApi} from '../api/client';
import BookCard from '../components/BookCard';
import {Search} from 'lucide-react';

export default function Home() {
    const [books, setBooks] = useState([]);
    const [loading, setLoading] = useState(true);
    const [search, setSearch] = useState({isbn: '', title: '', category: '', author: '', publisher: ''});

    const fetchBooks = async () => {
        setLoading(true);
        try {
            // Use the dynamic search if any param exists, else get all
            const hasParams = search.isbn || search.title || search.category || search.author || search.publisher;
            const res = hasParams ? await bookApi.search(search) : await bookApi.getAll();
            setBooks(res.data);
        } catch (err) {
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchBooks();
    }, []);

    const handleSearch = (e) => {
        e.preventDefault();
        fetchBooks();
    };

    return (<div className="max-w-7xl mx-auto px-4 py-8">
        {/* Search Bar */}
        <div className="bg-white p-6 rounded-xl shadow-sm border border-gray-100 mb-8">
            <form onSubmit={handleSearch} className="grid grid-cols-1 md:grid-cols-6 gap-4">
                <input
                    placeholder="Search by ISBN..."
                    className="p-3 border rounded-lg focus:ring-2 focus:ring-brand-500 outline-none"
                    value={search.isbn}
                    onChange={(e) => setSearch({...search, isbn: e.target.value})}
                />
                <input
                    placeholder="Search by Title..."
                    className="p-3 border rounded-lg focus:ring-2 focus:ring-brand-500 outline-none"
                    value={search.title}
                    onChange={(e) => setSearch({...search, title: e.target.value})}
                />
                <input
                    placeholder="Category (e.g. Science)"
                    className="p-3 border rounded-lg focus:ring-2 focus:ring-brand-500 outline-none"
                    value={search.category}
                    onChange={(e) => setSearch({...search, category: e.target.value})}
                />
                <input
                    placeholder="Author..."
                    className="p-3 border rounded-lg focus:ring-2 focus:ring-brand-500 outline-none"
                    value={search.author}
                    onChange={(e) => setSearch({...search, author: e.target.value})}
                />
                <input
                    placeholder="Publisher..."
                    className="p-3 border rounded-lg focus:ring-2 focus:ring-brand-500 outline-none"
                    value={search.publisher}
                    onChange={(e) => setSearch({...search, publisher: e.target.value})}
                />
                <button type="submit"
                        className="bg-brand-600 text-white font-bold rounded-lg hover:bg-brand-700 flex items-center justify-center gap-2">
                    <Search size={20}/> Search
                </button>
            </form>
        </div>

        {/* Grid */}
        {loading ? (<div className="text-center py-20 text-gray-500">Loading books...</div>) : (
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
                {books.map((book) => (<BookCard key={book.isbn} book={book}/>))}
            </div>)}
        {!loading && books.length === 0 && (
            <div className="text-center py-20 text-gray-400">No books found matching criteria.</div>)}
    </div>);
}