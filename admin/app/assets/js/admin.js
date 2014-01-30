function deleteConform(url) {
    var r = window.confirm("Do you want to delete this row?");
    if (r == true) {
        window.location.href = url
    }
}