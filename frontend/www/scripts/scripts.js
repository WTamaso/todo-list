function showPage(id) {
    document.getElementById("list").style.display = "none";
    document.getElementById("new").style.display = "none";

    document.getElementById(id).style.display = null;
}