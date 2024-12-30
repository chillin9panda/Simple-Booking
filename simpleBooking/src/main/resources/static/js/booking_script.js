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

// Form submit (AJAX)
document.getElementById('book-room-form').addEventListener('submit', function(event) {
    event.preventDefault();  // Prevent the default form submission

    // Collect form data
    const firstName = document.getElementById('guest_first_name').value;
    const lastName = document.getElementById('guest_last_name').value;
    const phoneNum = document.getElementById('contact-number').value;
    const email = document.getElementById('guest_email').value;

    // Create an object to store the data
    const data = {
        firstName: firstName,
        lastName: lastName,
        phoneNum: phoneNum,
        email: email
    };

    // Access CSRF Token and CSRF Header from meta tags
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    console.log("CSRF Token:", csrfToken);
    console.log("CSRF Header:", csrfHeader);

    // Use fetch to send the data to your backend (AJAX)
    fetch('/api/guests/add', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',  // Indicate we're sending JSON
            [csrfHeader]: csrfToken  // Use the CSRF header
        },
        body: JSON.stringify(data)  // Send the data as JSON
    })
    .then(response => response.json())  // Parse JSON response
    .then(responseData => {
        console.log('Success:', responseData);  // Handle success (e.g., show success message)
        alert('Booking successful!');
    })
    .catch(error => {
        console.error('Error:', error);  // Handle error
        alert('Error: ' + error.message);
    });
});

// Set the default section to show (Home) when the page loads
window.onload = showHome;
