let myButton = document.getElementById("myButton");

myButton.addEventListener("click", function () {
    fetchUsers();
    console.log('test');
})
async function fetchUsers() {
    try {
        const response = await fetch('http://localhost:8080/basic/users');
        if (!response.ok) {
            throw new Error(`HTTP error status: ${response.status}`);
        }
        const users = await response.json();

        const userContainer = document.getElementById('user-container');
        userContainer.innerHTML = users
            .map(user => `<p>ID: ${user.id}, Username: ${user.username}</p>`)
            .join('');
    } catch (error) {
        console.error('Error fetching users:', error);
        alert('Failed to fetch users. Please try again later.');
    }
}
