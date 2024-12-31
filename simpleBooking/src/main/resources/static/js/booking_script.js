// Dynamic date
const currentDate = new Date();

const formattedDate = currentDate.toLocaleDateString('en-US', {
    weekday: 'long',
    year: 'numeric',
    month: 'long',
    day: 'numeric'
});

document.getElementById('current-date').textContent = formattedDate;

// Show the Home section
function showHome() {
    hideAllSections();
    document.getElementById('home-section').style.display = 'block';
    document.getElementById('info-section').style.display = 'block';
}

// Show the Book Room section
function showBookRoom() {
    hideAllSections();
    document.getElementById('book-room-section').style.display = 'block';
}

// Show View Rooms section
function showViewRooms() {
    hideAllSections();
    document.getElementById('view-rooms-section').style.display = 'block';
}

// Function to hide all sections
function hideAllSections() {
    document.getElementById('home-section').style.display = 'none';  // Hide Home section
    document.getElementById('info-section').style.display = 'none';  // Hide Info section
    document.getElementById('book-room-section').style.display = 'none';  // Hide Book Room section
    document.getElementById('view-rooms-section').style.display = 'none'; // Hide View Rooms section
}

// Event Listeners for navigation links
document.getElementById('home-link').addEventListener('click', function (event) {
    event.preventDefault();
    showHome();
});

document.getElementById('book-room-link').addEventListener('click', function (event) {
    event.preventDefault();
    showBookRoom();
});

document.getElementById('view-rooms-link').addEventListener('click', function (event) {
    event.preventDefault();
    showViewRooms();
});


// Set the default section to show (Home) when the page loads
window.onload = showHome;
