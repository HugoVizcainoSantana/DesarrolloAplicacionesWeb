function Disable(checkboxID, toggleID) {
    var checkbox = document.getElementById(checkboxID);
    var toggle = document.getElementById(toggleID);
    updateToggle = checkbox.checked ? toggle.disabled = false : toggle.disabled = true;
}


