document.addEventListener("DOMContentLoaded", function () {
  const toggleButton = document.getElementById("toggleOptions");
  const optionsContainer = document.getElementById("optionsContainer");

  toggleButton.addEventListener("click", function () {
    if (optionsContainer.style.display === "flex") {
      optionsContainer.style.display = "none";
    } else {
      optionsContainer.style.display = "flex";
    }
  });
});
