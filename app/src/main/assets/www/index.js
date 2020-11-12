function createBlock() {
    let dom = $("<div></div>")
    dom.attr("class", "block")
    dom.attr("toggle", "false");
    dom.click(function () {
        if(dom.attr("toggle") == "true") {
            dom.attr("toggle", "false")
            dom.css("background", "#ffffff")
        } else {
            dom.attr("toggle", "true")
            dom.css("background", "#f171e4")
        }
    })
    return dom;
}

function createRow() {
    let length = 32
    let dom = $("<div></div>")
    dom.attr("class", "row");
    for (let i=0; i<length; i++) {
        dom.append(createBlock())
    }
    return dom;
}

function init() {
    let body = $("body")
    let frame = $("<div></div>")
    frame.attr("class", "frame")
    
    let row_no = 16;
    for (let i=0; i<row_no; i++) {
        frame.append(createRow())
    }

    body.append(frame)
}

function getBits() {
    let bits = [];
    let color = 248
    bits.push(color.toString(2) + "00000000");
    $(".frame .block").each(function(i, dom) {
        let toggle = $(dom).attr("toggle")
        if (toggle == "true") bits.push("00000001")
        else bits.push("00000000")
    })
    console.log(bits)
    return bits.join("");
}

init()
console.log("hello world")