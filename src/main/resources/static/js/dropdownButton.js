document.addEventListener("DOMContentLoaded", function () {
    const selectBtn = document.getElementById("selectedOption");
    const optionsContainer = document.getElementById("optionsContainer");

    selectBtn.addEventListener("click", function () {
        if (optionsContainer.classList.contains('show')) {
            optionsContainer.classList.remove('show');
        } else {
            optionsContainer.classList.add('show');
        }
    });

    document.querySelectorAll('.option-link').forEach(option => {
        option.addEventListener('click', function () {
            selectBtn.textContent = this.getAttribute('data-value');
            optionsContainer.classList.remove('show');
        });
    });
});
