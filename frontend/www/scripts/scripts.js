function showPage(id) {
    document.getElementById("list").style.display = "none";
    document.getElementById("new").style.display = "none";

    document.getElementById(id).style.display = null;
}

function ajax(method, url, body, onSuccess, onFail) {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4) {
            if (this.status >= 200 && this.status < 300) {
                onSuccess(this.responseText);
            } else {
                onFail(this.responseText);
            }
        }
    };
    xhttp.open(method, url, true);
    switch (method) {
        case ("GET"):
        case ("DELETE"):
            xhttp.send();
            break;
        case ("PUT"):
        case ("POST"):
            xhttp.send(body);
            break;
        default:
            break;
    }
}

function getParamString(params) {
    var str = Object.keys(params)
    .map(function (k) {
        return params[k] ? encodeURIComponent(k) + '=' + encodeURIComponent(params[k]) : ""
    }).join('&');
    return "?" + str;
}

function getTaskList(status, filter, limit, offset) {
    var data = {
        status: status,
        filter: filter,
        limit: limit,
        offset: offset
    };

    ajax("GET", "http://localhost:8080/todo/api/tasks/" + getParamString(data), null,
        function (result) {
            document.getElementById("result").innerText = "Success =>>" + result;
        },
        function (result) {
            document.getElementById("result").innerText = "Failure =>>" + result;
        });
}

function getTask(id) {
    ajax("GET", "http://localhost:8080/todo/api/tasks/" + id, null,
        function (result) {
            document.getElementById("result").innerText = "Success =>>" + result;
        },
        function (result) {
            document.getElementById("result").innerText = "Failure =>>" + result;
        });
}

function deleteTask(id) {
    ajax("DELETE", "http://localhost:8080/todo/api/tasks/" + id, null,
        function (result) {
            document.getElementById("result").innerText = "Success =>>" + result;
        },
        function (result) {
            document.getElementById("result").innerText = "Failure =>>" + result;
        });
}

function createTask(title, description) {
    ajax("POST", "http://localhost:8080/todo/api/tasks/", JSON.stringify({title: title, description: description}),
        function (result) {
            document.getElementById("result_form").innerText = "Success =>>" + result;
        },
        function (result) {
            document.getElementById("result_form").innerText = "Failure =>>" + result;
        });
}

function updateTask(id, status, title, description) {
    ajax("PUT", "http://localhost:8080/todo/api/tasks/" + id, JSON.stringify({
            status: status,
            title: title,
            description: description
        }),
        function (result) {
            document.getElementById("result_form").innerText = "Success =>>" + result;
        },
        function (result) {
            document.getElementById("result_form").innerText = "Failure =>>" + result;
        });
}