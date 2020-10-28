function search_help(tags) {
    $("#searchValue").autocomplete({
        source: tags
    });
}

function onInput() {
    var input = document.forms.searchForm.searchValue.value;
    if (input != '') {
        ssyl = `http://5.200.47.32:81/api/search/tag/${input}`;
        console.log(ssyl);
        $.ajax(
            {
                type: 'GET',
                url: ssyl,
                dataType: 'json',
                success: function (data, textStatus) {
                    console.log(data);
                    search_help(data);
                }
            })
    }
}

$('.form').delegate("#add", "click", function () {
    console.log('works');
    mark = document.forms.regForm.mark.value;
    diameter = document.forms.regForm.diameter.value;
    packing = document.forms.regForm.packing.value;
    plav = document.forms.regForm.plav.value;
    part = document.forms.regForm.part.value;
    mass = document.forms.regForm.mass.value;
    comment = document.forms.regForm.comment.value;
    console.log(mark, diameter, packing, plav, part, mass, comment);
    ssyl = `http://5.200.47.32:81/api/position/add?manufacturer=a&mark=${mark}&diameter=${diameter}&packing=${packing}&plav=${plav}&part=${part}&mass=${mass}&comment=${comment}`;
    console.log(ssyl);
    $.ajax(
        {
            type: 'POST',
            url: ssyl,
            dataType: "json",
            success: function (data, textStatus) {
                console.log(data);
                window.open(`product.html?id=${data}`, '_blank');
                //$('.form').css("display", "none");
                document.forms.regForm.mark.value = '';
                document.forms.regForm.diameter.value = '';
                document.forms.regForm.plav.value = '';
                document.forms.regForm.part.value = '';
                document.forms.regForm.mass.value = '';
                document.forms.regForm.comment.value = '';
                //$('.print').html(`<a href="#" onclick="PrintImage('http://5.200.47.32:81/api/code/${data}'); return false;">PRINT</a>`);
                //$('.print').css("display", "inline");
            },
            error: function (data, textStatus) {
                $('.error').fadeIn();
                setTimeout(function () {
                    $('.error').fadeOut();
                }, 2000);
            }
        });
})

function card(id, mark, diameter, packing, comment, part, plav, mass) {
    return `<a href="product.html?id=${id}" class="card product mx-1 my-1" style="width:30%" id="${id}">
        <div class="card-body"> 
            <p class="card-text">Марка: ${mark}</p>
            <p class="card-text">Диаметр: ${diameter}</p>
            <p class="card-text">Упаковка: ${packing}</p>
            <p class="card-text">Партия: ${part}</p>
            <p class="card-text">Сплав: ${plav}</p>
            <p class="card-text">Вес: ${mass}</p>
            <p class="card-text">Комментарий: ${comment}</p>
        </div>
    </a>`;
}

function card_without_comm(id, mark, diameter, packing, part, plav, mass) {
    return `<a href="product.html?id=${id}" class="card product mx-1 my-1" style="width:30%" id="${id}">
        <div class="card-body"> 
            <p class="card-text">Марка: ${mark}</p>
            <p class="card-text">Диаметр: ${diameter}</p>
            <p class="card-text">Упаковка: ${packing}</p>
            <p class="card-text">Партия: ${part}</p>
            <p class="card-text">Сплав: ${plav}</p>
            <p class="card-text">Вес: ${mass}</p>
        </div>
    </a>`;
}

$('#controlBtns').delegate('#all', "click", function () {
    console.log('ass we can');
    products = "";
    $.ajax(
        {
            type: 'GET',
            url: 'http://5.200.47.32:81/api/positions',
            dataType: 'json',
            success: function (data, textStatus) {
                console.log(data);
                data.forEach(function (product) {
                    if (product.comment == null || product.comment == "") {
                        products = products + card_without_comm(product.id, product.mark, product.diameter, product.packing, product.part, product.plav, product.mass);
                    } else {
                        products = products + card(product.id, product.mark, product.diameter, product.packing, product.comment, product.part, product.plav, product.mass);
                    }
                });
                $('#cards').html(products);
                $('#hide').attr('class', 'btn btn-outline-info btn-block mainBtns mb-2 hide');
            }
        }
    )
})

$('#controlBtns').delegate('#openSearch', "click", function () {
    $('#cards').html(
        `<form name="searchForm" class="col-12">
         <div class="row">
            <div class="col-9 ui-widget">
               <input class="form-control col-10" name="searchValue" id="searchValue" oninput='onInput();'/>
            </div>
            <div class="col-3">
               <button type="button" class="btn btn-outline-info btn-block mb-2 search" id="search">Найти</button>
            </div>
         </div>
      </form>
      <div class="searchContent col-12 row"></div>`);
})

$('#controlBtns').delegate('#hide', 'click', function () {
    $('#cards').html('');
    $('#hide').attr('class', 'btn btn-outline-info btn-block mb-2 hide d-none');
})

$('#controlBtns').delegate("#add", "click", function () {
    console.log("dlkf ");
    $('.form').css('display', 'inline');
});

$('.form').delegate('.close-block', 'click', function () {
    $('.form').fadeOut();
})

$('#cards').delegate('#search', "click", function () {
    products = "";
    val = document.forms.searchForm.searchValue.value;
    console.log(val);
    ssyl = `http://5.200.47.32:81/api/search/${val}`
    $.ajax(
        {
            type: 'GET',
            url: ssyl,
            dataType: 'json',
            success: function (data, textStatus) {
                console.log(data);
                data.forEach(function (product) {
                    if (product.comment == null || product.comment == "") {
                        products = products + card_without_comm(product.id, product.mark, product.diameter, product.packing, product.part, product.plav, product.mass);
                    } else {
                        products = products + card(product.id, product.mark, product.diameter, product.packing, product.comment, product.part, product.plav, product.mass);
                    }
                });
                $('.searchContent').html(products);
                $('#hide').attr('class', 'btn btn-outline-info btn-block mb-2 hide');
            },
            error: function () {
                console.log(data);
                profucts = data;
            }
        }
    );
    console.log(products);
})

//$('#content').delegate('.product', 'click', function(){
//   console.log(this.id);
//   var PageLink = `product.html?id=${this.id}`;
//   var pwa = window.open(PageLink, '_blank');
//})

function ImagetoPrint(source) {
    return "<html><title>Штрих-код</title><head><scri" +
        "pt>function step1(){\n" +
        "setTimeout('step2()', 10);}\n" +
        "function step2(){window.print();window.close()}\n" +
        "</scri" + "pt></head><body onload='step1()'>\n" +
        `<p style="font-size: 20pt;">Какая-то инфа</p>` +
        "<img src='" + source + "' /></body></html>";
}

function PrintImage(source) {
    var Pagelink = "about:blank";
    var pwa = window.open(Pagelink, "_new");
    pwa.document.open();
    console.log(ImagetoPrint(source));
    pwa.document.write(ImagetoPrint(source));
    pwa.document.close();
}

function compare() {
    if (a < b)
        return -1;
    if (a > b)
        return 1;
    return 0;
}

function sorting() {
    console.log($('#sort option:selected').val());
    sort_data = [];
    $.ajax(
        {
            type: 'GET',
            url: 'http://5.200.47.32:81/api/positions',
            dataType: 'json',
            async: false,
            success: function (data, textStatus) {
                console.log(data);
                switch ($('#sort option:selected').val()) {
                    case 'mark':
                        sort_data = data.sort(function (a, b) {
                            return (a.mark > b.mark) ? 1 : ((b.mark > a.mark) ? -1 : 0);
                        });
                        break;
                    case 'diameter':
                        data.sort(function (a, b) {
                            return (a.diameter > b.diameter) ? 1 : ((b.diameter > a.diameter) ? -1 : 0);
                        });
                        break;
                    case 'packing':
                        sort_data = data.sort(function (a, b) {
                            return (a.packing > b.packing) ? 1 : ((b.packing > a.packing) ? -1 : 0);
                        });
                        break;
                    case 'part':
                        sort_data = data.sort(function (a, b) {
                            return (a.part > b.part) ? 1 : ((b.part > a.part) ? -1 : 0);
                        });
                        break;
                    case 'splav':
                        sort_data = data.sort(function (a, b) {
                            return (a.plav > b.plav) ? 1 : ((b.plav > a.plav) ? -1 : 0);
                        });
                        break;
                    case 'mass':
                        sort_data = data.sort(function (a, b) {
                            return (a.mass > b.mass) ? 1 : ((b.mass > a.mass) ? -1 : 0);
                        });
                        break;
                }
                products = ``;
                data.forEach(function (product) {
                    if (product.comment == null || product.comment == "") {
                        products = products + card_without_comm(product.id, product.mark, product.diameter, product.packing, product.part, product.plav, product.mass);
                    } else {
                        products = products + card(product.id, product.mark, product.diameter, product.packing, product.comment, product.part, product.plav, product.mass);
                    }
                })
                $('#cards').html(products);
            }
        }
    )
    //console.log(sort_data);
}

$('#sort').delegate('#diameter', 'click', function () {
    console.log(this.value);
    sort(this.value);
})

function help_man() {
    input = document.forms.regForm.manufacturer.value;
    if (input == null || input == '') {
        $.ajax({
            type: 'GET',
            url: `http://5.200.47.32:81/api/search/manufacturer/-`,
            dataType: 'json',
            success: function (data, textStatus) {
                console.log(data);
                $("#manufacturer").autocomplete({
                    source: data
                });
            }
        })
    } else {
        $.ajax({
            type: 'GET',
            url: `http://5.200.47.32:81/api/search/manufacturer/${input}`,
            dataType: 'json',
            success: function (data, textStatus) {
                console.log(data);
                $("#manufacturer").autocomplete({
                    source: data
                });
            }
        })
    }
}

$(function () {
    var availableTags = ["Моток",
        "К300",
        "Д300",
        "К415",
        "Д415"]
    $.ajax({
        type: 'GET',
        url: `http://5.200.47.32:81/api/position/marks`,
        dataType: 'json',
        success: function (data, textStatus) {
            console.log(data);
            tags = '';
            data.forEach(function (tag) {
                tags += `<input name="markType" class="form-check-input" type="checkbox" id="${tag}" value="${tag}">
            <label class="form-check-label" for="${tag}">
            ${tag}
            </label><div class="w-100"></div>`;
            })
            $('.marks').html(tags);
        }
    })
    $.ajax({
        type: 'GET',
        url: `http://5.200.47.32:81/api/position/diameter`,
        dataType: 'json',
        success: function (data, textStatus) {
            console.log(data);
            $("#slider").slider({
                animate: "slow",
                range: true,
                min: parseFloat(data[0]),
                max: parseFloat(data[1]),
                values: [parseFloat(data[0]), parseFloat(data[1])],
                step: 0.1,
                slide: function (event, ui) {
                    $("#result-slider").text("от " + ui.values[0] + " до " + ui.values[1]);
                }
            });
            $("#result-slider").text("от " + $("#slider").slider("values", 0) + " до " + $("#slider").slider("values", 1));
        }
    })
    tags = '';
    availableTags.forEach(function (tag) {
        tags += `<input name="packingType" class="form-check-input" type="checkbox" id="${tag}" value="${tag}">
      <label class="form-check-label" for="${tag}">
      ${tag}
      </label><div class="w-100"></div>`;
    })
    $('.packing').html(tags);
})

$('.filter').delegate('#filtrate', 'click', function () {
    selected_marks = [];
    selected_packings = [];
    $("input:checkbox[name=markType]:checked").each(function () {
        selected_marks.push($(this).val());
    });
    $("input:checkbox[name=packingType]:checked").each(function () {
        selected_packings.push($(this).val());
    });
    min = $("#slider").slider("values", 0);
    max = $("#slider").slider("values", 1);
    console.log(selected_marks, selected_packings, min, max);
    object = {
        "mark": selected_marks,
        "packing": selected_packings,
        "DL": min,
        "DM": max
    }
    $.ajax({
        type: 'GET',
        url: `http://5.200.47.32:81/api/filter`,
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(object),
        dataType: 'json',
        cache: false,
        success: function (data, textStatus) {
            console.log(data);
        },
        error: function (data, textStatus) {
            console.log(data);
        }
    })
})