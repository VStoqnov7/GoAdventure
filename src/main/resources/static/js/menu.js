    document.addEventListener('DOMContentLoaded', () => {
        const menu = document.querySelector('.menu');
        const menuButton = document.querySelector('.menu-button');

        menuButton.addEventListener('click', (event) => {
            event.preventDefault();

            if (menu.style.display === 'none' || menu.style.display === '') {
                menu.style.display = 'grid';
            } else {
                menu.style.display = 'none';
            }
        });
    });