const API_BASE = '/book';

let currentPage = 0;
const pageSize = 5;
let currentSort = 'id';

document.addEventListener('DOMContentLoaded', () => {
    loadBooks();

    document.getElementById('addBookForm').addEventListener('submit', handleAddBook);
    document.getElementById('refreshBtn').addEventListener('click', loadBooks);
    
    document.getElementById('prevPage').addEventListener('click', () => {
        if (currentPage > 0) {
            currentPage--;
            loadBooks();
        }
    });

    document.getElementById('nextPage').addEventListener('click', () => {
        currentPage++;
        loadBooks();
    });

    // Modal listeners
    document.getElementById('closeModal').addEventListener('click', closeEditModal);
    document.getElementById('editBookForm').addEventListener('submit', handleEditBook);
});

async function loadBooks() {
    const tableBody = document.getElementById('bookTableBody');
    try {
        const response = await fetch(`${API_BASE}/books?page=${currentPage}&size=${pageSize}&sortBy=${currentSort}&sortDir=asc`);
        const result = await response.json();
        
        if (result.status === 200 && result.data) {
            const pageData = result.data;
            const books = pageData.content;
            
            tableBody.innerHTML = '';
            
            if (books.length === 0) {
                tableBody.innerHTML = `<tr><td colspan="6" style="text-align:center; color: var(--text-secondary)">No books found.</td></tr>`;
            } else {
                books.forEach((book, index) => {
                    const row = document.createElement('tr');
                    // Compute absolute index across pages
                    const displayIndex = (currentPage * pageSize) + index + 1;
                    row.innerHTML = `
                        <td>${displayIndex}</td>
                        <td><strong>${book.title}</strong></td>
                        <td>${book.author}</td>
                        <td>${book.year}</td>
                        <td style="color: var(--success); font-weight: 500;">$${book.price.toFixed(2)}</td>
                        <td class="actions">
                            <button class="btn-icon" title="Edit" onclick="openEditModal(${book.id}, '${escapeHtml(book.title)}', '${escapeHtml(book.author)}', ${book.price}, ${book.year})">
                                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"></path><path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"></path></svg>
                            </button>
                            <button class="btn-icon delete" title="Delete" onclick="deleteBook(${book.id})">
                                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="3 6 5 6 21 6"></polyline><path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path><line x1="10" y1="11" x2="10" y2="17"></line><line x1="14" y1="11" x2="14" y2="17"></line></svg>
                            </button>
                        </td>
                    `;
                    tableBody.appendChild(row);
                });
            }

            // Update pagination
            document.getElementById('pageInfo').innerText = `Page ${pageData.number + 1} of ${pageData.totalPages || 1}`;
            document.getElementById('prevPage').disabled = pageData.first;
            document.getElementById('nextPage').disabled = pageData.last;
            
        } else {
            showMessage('load', result.message || 'Error loading books', 'error');
        }
    } catch (e) {
        console.error(e);
        tableBody.innerHTML = `<tr><td colspan="6" style="text-align:center; color: var(--danger)">Failed to load data. Ensure server is running.</td></tr>`;
    }
}

async function handleAddBook(e) {
    e.preventDefault();
    const form = e.target;
    
    const payload = {
        title: form.title.value,
        author: form.author.value,
        price: parseFloat(form.price.value),
        year: parseInt(form.year.value, 10)
    };

    const spinner = document.getElementById('submitSpinner');
    const submitBtn = document.getElementById('submitBtn');
    
    try {
        spinner.classList.remove('hidden');
        submitBtn.disabled = true;

        const response = await fetch(API_BASE, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload)
        });

        const result = await response.json();

        if (response.ok) {
            showMessage('formMessage', 'Book added successfully!', 'success');
            form.reset();
            loadBooks(); // refresh list
        } else {
            // Check for validation errors if any (Spring Validation format)
            if(result.message) {
                 showMessage('formMessage', result.message, 'error');
            } else if(result.errors) {
                 showMessage('formMessage', result.errors[0].defaultMessage || 'Validation error', 'error');
            } else {
                 showMessage('formMessage', 'Failed to add book', 'error');
            }
        }
    } catch (e) {
        showMessage('formMessage', 'Network error. Could not add book.', 'error');
    } finally {
        spinner.classList.add('hidden');
        submitBtn.disabled = false;
    }
}

async function deleteBook(id) {
    if (!confirm('Are you sure you want to delete this book?')) return;
    
    try {
        const response = await fetch(`${API_BASE}/${id}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            // If deleting the last item on a page, try going back one page
            const tableRows = document.querySelectorAll('#bookTableBody tr').length;
            if (tableRows === 1 && currentPage > 0) {
                currentPage--;
            }
            loadBooks();
        } else {
            alert('Failed to delete book');
        }
    } catch (e) {
        alert('Error communicating with server');
    }
}

// Modal handling
function openEditModal(id, title, author, price, year) {
    document.getElementById('editId').value = id;
    document.getElementById('editTitle').value = title;
    document.getElementById('editAuthor').value = author;
    document.getElementById('editPrice').value = price;
    document.getElementById('editYear').value = year;
    
    const msgDiv = document.getElementById('editMessage');
    msgDiv.className = 'message hidden';
    msgDiv.innerText = '';
    
    document.getElementById('editModal').classList.remove('hidden');
}

function closeEditModal() {
    document.getElementById('editModal').classList.add('hidden');
}

async function handleEditBook(e) {
    e.preventDefault();
    const id = document.getElementById('editId').value;
    const payload = {
        title: document.getElementById('editTitle').value,
        author: document.getElementById('editAuthor').value,
        price: parseFloat(document.getElementById('editPrice').value),
        year: parseInt(document.getElementById('editYear').value, 10)
    };

    try {
        const response = await fetch(`${API_BASE}/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        const result = await response.json();

        if (response.ok) {
            closeEditModal();
            loadBooks();
        } else {
             showMessage('editMessage', result.message || 'Failed to update book', 'error');
        }
    } catch (e) {
        showMessage('editMessage', 'Network error.', 'error');
    }
}

// Utils
function showMessage(elementId, msg, type) {
    const el = document.getElementById(elementId);
    if (!el) return;
    el.textContent = msg;
    el.className = `message ${type}`;
    el.classList.remove('hidden');
    
    setTimeout(() => {
        el.classList.add('hidden');
    }, 5000);
}

function escapeHtml(unsafe) {
    if (!unsafe) return '';
    return unsafe
         .replace(/&/g, "&amp;")
         .replace(/</g, "&lt;")
         .replace(/>/g, "&gt;")
         .replace(/"/g, "&quot;")
         .replace(/'/g, "&#039;");
}
