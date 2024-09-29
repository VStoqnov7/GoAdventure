const lists = document.querySelectorAll('.user-list, .adventure-list');

lists.forEach(list => {
    let isDown = false;
    let startY;
    let scrollTop;

    list.addEventListener('mousedown', (e) => {
        isDown = true;
        list.classList.add('active');
        startY = e.pageY - list.offsetTop;
        scrollTop = list.scrollTop;
        list.style.cursor = 'grabbing';
    });

    list.addEventListener('mouseleave', () => {
        isDown = false;
        list.classList.remove('active');
        list.style.cursor = 'grab';
    });

    list.addEventListener('mouseup', () => {
        isDown = false;
        list.classList.remove('active');
        list.style.cursor = 'grab';
    });

    list.addEventListener('mousemove', (e) => {
        if (!isDown) return;
        e.preventDefault();
        const y = e.pageY - list.offsetTop;
        const walk = (y - startY) * 1.5;
        list.scrollTop = scrollTop - walk;
    });
});

document.addEventListener('DOMContentLoaded', () => {
    const userList = document.querySelector('.user-list');
    const adventureList = document.querySelector('.adventure-list');
    const scrollIndicator1 = document.querySelector('.scroll-indicator-1');
    const scrollIndicator2 = document.querySelector('.scroll-indicator-2');

    const updateAdventureScrollIndicator = () => {
        const atBottom = adventureList.scrollTop + adventureList.clientHeight >= adventureList.scrollHeight - 2;
        scrollIndicator1.style.display = atBottom ? 'none' : 'block';
    };

    const updateUserScrollIndicator = () => {
        const atBottom = userList.scrollTop + userList.clientHeight >= userList.scrollHeight - 2;
        const contentTooTall = userList.scrollHeight > userList.clientHeight;
        scrollIndicator2.style.display = (atBottom || !contentTooTall) ? 'none' : 'block';
    };

    userList.addEventListener('scroll', updateUserScrollIndicator);
    adventureList.addEventListener('scroll', updateAdventureScrollIndicator);

    updateUserScrollIndicator();
    updateAdventureScrollIndicator();
});
