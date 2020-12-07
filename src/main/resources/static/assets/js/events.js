function search_help(tags) {
    $("#searchValue").autocomplete({
        source: tags
    });
}

function plavSearch_help(tags) {
    $("#plavSearchValue").autocomplete({
        source: tags
    });
}

function onInput() {
    var input = document.forms.searchForm.searchValue.value;
    if (input != '') {
        ssyl = `http://5.200.47.32/api/search/tag/${input}`;
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

function onPlavSearchInput() {
    var input = document.forms.plavSearchForm.plavSearchValue.value;
    obj = {
        "plav": input
    };
    if (input != '') {
        ssyl = `http://ferro-trade.ru/api/search/plavka/tags?plav=${input}`;
        $.ajax(
            {
                type: 'POST',
                url: ssyl,
                dataType: 'json',
                success: function (data, textStatus) {
                    console.log(data);
                    plavSearch_help(data);
                },
                error: function (data, textStatus) {
                    console.log(data);
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
    manufacturer = document.forms.regForm.manufacturer.value;
    console.log(mark, diameter, packing, plav, part, mass, comment);
    ssyl = `http://5.200.47.32/api/position/add?manufacturer=${manufacturer}&mark=${mark}&diameter=${diameter}&packing=${packing}&plav=${plav}&part=${part}&mass=${mass}&comment=${comment}`;
    console.log(ssyl);
    $.ajax(
        {
            type: 'POST',
            url: ssyl,
            dataType: "json",
            success: function (data, textStatus) {
                console.log(data);
                window.open(`product?id=${data}`, '_blank');
                //$('.form').css("display", "none");
                document.forms.regForm.mark.value = '';
                document.forms.regForm.diameter.value = '';
                document.forms.regForm.plav.value = '';
                document.forms.regForm.part.value = '';
                document.forms.regForm.mass.value = '';
                document.forms.regForm.comment.value = '';
                document.forms.regForm.manufacturer.value = '';
                //$('.print').html(`<a href="#" onclick="PrintImage('http://5.200.47.32/api/code/${data}'); return false;">PRINT</a>`);
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

$('.addManyFormCheck').on('change', function () {
    $('input[name="addManyFormCheck"]').val($(this).is(':checked') ? 'true' : 'false');
});

$(".addManyForm").delegate('.addManyFormCheck', 'click', function () {
    console.log($(this).is(':checked'), $(this).attr('id'));
    id = $(this).attr('id') + 'Many';
    if ($(this).is(':checked'))
        $(`#${id}`).prop('disabled', false);
    else
        $(`#${id}`).prop('disabled', true);
})

$('.addManyForm').delegate("#addMany", "click", function () {
    console.log('works');
    mark = document.forms.regManyForm.markMany.value;
    diameter = document.forms.regManyForm.diameterMany.value;
    packing = document.forms.regManyForm.packingMany.value;
    plav = document.forms.regManyForm.plavMany.value;
    part = document.forms.regManyForm.partMany.value;
    mass = document.forms.regManyForm.massMany.value;
    comment = document.forms.regManyForm.commentMany.value;
    manufacturer = document.forms.regManyForm.manufacturerMany.value;
    count = document.forms.regManyForm.number.value;
    console.log(mark, diameter, packing, plav, part, mass, comment, manufacturer);
    object = {
        "part": part,
        "mark": mark,
        "diameter": diameter,
        "plav": plav
    };
    $.ajax(
        {
            type: 'POST',
            url: `http://ferro-trade.ru/api/add/validate`,
            contentType: "application/json; charset=utf-8",
            dataType: 'json',
            data: JSON.stringify(object),
            success: function (data, textStatus) {
                console.log(data);
                if (data) {
                    $('.addManyForm').fadeOut();
                    checked = [];
                    elems = $("input:checkbox[name=addManyFormCheck]:checked");
                    for (i = 0; i < elems.length; i++) {
                        checked.push(elems[i].id);
                        elems[i].checked = false;
                        $(`#${elems[i].id + 'Many'}`).prop('disabled', 'true');
                    }
                    console.log(checked);
                    products = '';
                    product = card_for_many_reg(checked.includes('mark'), checked.includes('diameter'), checked.includes('packing'), checked.includes('part'),
                        checked.includes('plav'), checked.includes('mass'), checked.includes('manufacturer'), checked.includes('comment'), mark, diameter, packing,
                        part, plav, mass, manufacturer, comment);
                    for (i = 0; i < count; i++) {
                        products += product;
                    }
                    btn = `<div class="row col-12">
                    <button class="btn btn-outline-success btn-block col-10" id="regMany">Зарегистрировать</button>
                    <div class="col-2 text-center">Вес: <span class="addManyMass">0</span></div>
                    </div>`;
                    $("#content").html(btn + products);
                    $("#content").unbind('click').delegate('#regMany', 'click', function () {
                        $("#regMany").prop("disabled", true);
                        elems = $('.productInput');
                        objs = [];
                        for (i = 0; i < elems.length; i++) {
                            if (checked.includes('mark'))
                                queryMark = mark;
                            else
                                queryMark = $(elems[i].lastChild).find('#mark')[0].value;

                            if (checked.includes('diameter'))
                                queryDiameter = diameter;
                            else
                                queryDiameter = $(elems[i].lastChild).find('#diameter')[0].value;

                            if (checked.includes('packing'))
                                queryPacking = packing;
                            else
                                queryPacking = $(elems[i].lastChild).find('#packing')[0].value;

                            if (checked.includes('part'))
                                queryPart = part;
                            else
                                queryPart = $(elems[i].lastChild).find('#part')[0].value;

                            if (checked.includes('plav'))
                                queryPlav = plav;
                            else
                                queryPlav = $(elems[i].lastChild).find('#plav')[0].value;

                            if (checked.includes('mass'))
                                queryMass = mass;
                            else
                                queryMass = $(elems[i].lastChild).find('#mass')[0].value;

                            if (checked.includes('manufacturer'))
                                queryManufacturer = manufacturer;
                            else
                                queryManufacturer = $(elems[i].lastChild).find('#manufacturer')[0].value;

                            if (checked.includes('comment'))
                                queryComment = comment;
                            else
                                queryComment = $(elems[i].lastChild).find('#comment')[0].value;
                            queryMass = parseFloat(queryMass.replaceAll(',', '.'));
                            console.log(queryMark, queryDiameter, queryPacking, queryPart, queryPlav, queryMass, queryManufacturer, queryComment);
                            obj = {
                                "mark": queryMark,
                                "diameter": queryDiameter,
                                "packing": queryPacking,
                                "comment": queryComment,
                                "part": queryPart,
                                "plav": queryPlav,
                                "manufacturer": queryManufacturer,
                                "mass": queryMass
                            }
                            objs.push(obj);
                        }
                        console.log(objs);
                        $.ajax({
                            type: 'POST',
                            url: `http://5.200.47.32:80/api/multipleAdd`,
                            contentType: "application/json; charset=utf-8",
                            data: JSON.stringify(objs),
                            dataType: 'json',
                            success: function (data, textStatus) {
                                console.log(data);
                                products = `<div class="row col-12"><button class="btn btn-outline-info btn-block mb-2" onclick="PrintCode(${data.id}); return false;">Распечатать позиции</button></div>
                     <div class="row col-12"><button class="btn btn-outline-info btn-block mb-2" onclick="PrintPackage(${data.package}); return false;">Распечатать поддон</button></div>`;
                                data.id.forEach(function (id) {
                                    products += card_to_print(id);
                                })
                                $("#regMany").prop("disabled", false);
                                $("#content").html(products);
                            }
                        })
                    })
                } else {
                    $(".partError").css('display', 'inline');
                }
            }
        })
})

$('.updateForm').delegate("#upd", "click", function () {
    id = document.forms.updForm.updId.value;
    mark = document.forms.updForm.updMark.value;
    diameter = document.forms.updForm.updDiameter.value;
    packing = document.forms.updForm.updPacking.value;
    plav = document.forms.updForm.updPlav.value;
    part = document.forms.updForm.updPart.value;
    mass = document.forms.updForm.updMass.value;
    mass = parseFloat(mass).toFixed(2);
    comment = document.forms.updForm.updComment.value;
    manufacturer = document.forms.updForm.updManufacturer.value;
    console.log(mark, diameter, packing, plav, part, mass, comment, manufacturer, id);
    object = {
        "mark": mark,
        "diameter": diameter,
        "packing": packing,
        "comment": comment,
        "part": part,
        "plav": plav,
        "manufacturer": manufacturer,
        "mass": mass,
        "id": id
    }
    $.ajax(
        {
            type: 'POST',
            url: `http://ferro-trade.ru/api/edit/save`,
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(object),
            success: function (data, textStatus) {
                console.log(data);
            },
            error: function (data, textStatus) {
                console.log(data);
            }
        })
})

$('.updateForm').delegate("#loadInfoById", "click", function () {
    id = document.forms.updForm.updId.value;
    console.log(id);
    $.ajax(
        {
            type: 'GET',
            url: `http://ferro-trade.ru/api/getById/${id}`,
            contentType: "application/json; charset=utf-8",
            dataType: 'json',
            success: function (data, textStatus) {
                console.log(data);
                document.forms.updForm.updMark.value = data.mark;
                document.forms.updForm.updDiameter.value = data.diameter;
                document.forms.updForm.updPacking.value = data.packing;
                document.forms.updForm.updPlav.value = data.plav;
                document.forms.updForm.updPart.value = data.part;
                document.forms.updForm.updMass.value = data.mass;
                document.forms.updForm.updComment.value = data.comment;
                document.forms.updForm.updManufacturer.value = data.manufacturer;
            },
            error: function (data, textStatus) {
                console.log(data);
            }
        })
})

$('#controlBtns').delegate('#all', "click", function () {
    $("#content").html(`
   <div class="col-9 column">
      <div class="sortSwitch row">
      <div class="sortFilter mr-5 col-3">
         <label for="sort">Сортировка</label>
         <select name="sort form-control" onchange="sorting()" id="sort">
            <option value="mark">Марка</option>
            <option value="diameter" id="diameter">Диаметр</option>
            <option value="packing">Упаковка</option>
            <option value="part">Партия</option>
            <option value="splav">Плавка</option>
            <option value="mass">Вес</option>
         </select>
      </div>
      <div>Таблица</div>
      <div class="checkbox checbox-switch switch-primary">
         <label>
            <input type="checkbox" name="" checked="" id="switcher"/>
            <span></span>
         </label>
      </div>
      <div>Карточки</div>
      </div>
      <div class="cards row" id="cards"></div>
   </div>
   <div class="col-3 filter">
   <h5>Фильтр</h5>
   <form>
      <div class="form-group">
      <label for="" class="col-sm-2 col-form-label">Марка</label>
      <div class="marks"></div>
      </div>
      <div class="form-group">
      <label for="inputPassword3" class="col-sm-2 col-form-label">Диаметр</label>
      <div class="diameters" id="slider"></div>
      <b>Значение ползунка: <span style="color:green" id="result-slider"></span></b>
      </div>
      <div class="form-group">
      <label for="" class="col-sm-2 col-form-label">Упаковка</label>
      <div class="packing"></div>
   </form>
   <button class="btn btn-outline-success btn-block" id="filtrate" type="button">Отфильтровать</button>
   </div>`);
    fillFilter();
    $("#switcher").prop('checked', true);
    products = "";
    $.ajax(
        {
            type: 'GET',
            url: 'http://5.200.47.32:80/api/positions',
            dataType: 'json',
            success: function(data, textStatus)
            {
                console.log(data);
                products = cards(data);
                $('#cards').html(products);
            }
        }
    )
})

$('#controlBtns').delegate('#unite', "click", function(){
    $('#content').html(
        `<form name="uniteForm" class="col-12 uniteForm">
         <div class="row">
            <div class="col-9 ui-widget">
               <input class="form-control col-10" name="uniteValues" id="uniteValues" autocomplete="off/>
            </div>
            <div class="col-3">
               <button type="button" class="btn btn-outline-info btn-block mb-2 unite" id="unite">Объединить</button>
            </div>
         </div>
      </form>
      <div class="uniteContent col-12 row"></div>`);
    bind_unite();
})

$('#controlBtns').delegate('#openSearch', "click", function () {
    $('#content').html(
        `<form name="searchForm" class="col-12">
         <div class="row">
            <div class="col-9 ui-widget">
               <input class="form-control col-10" name="searchValue" id="searchValue" oninput='onInput();' autocomplete="off"/>
            </div>
            <div class="col-3">
               <button type="button" class="btn btn-outline-info btn-block mb-2 search" id="search">Найти</button>
            </div>
         </div>
      </form>
      <div class="searchContent col-12 row"></div>`);
    bind_search();
})

$('#controlBtns').delegate('#openPlavSearch', "click", function () {
    $('#content').html(
        `<form name="plavSearchForm" class="col-12">
         <div class="row">
            <div class="col-9 ui-widget">
               <input class="form-control col-10" name="plavSearchValue" id="plavSearchValue" oninput='onPlavSearchInput();'/>
            </div>
            <div class="col-3">
               <button type="button" class="btn btn-outline-info btn-block mb-2 plavSearch" id="plavSearch">Найти по плавке</button>
            </div>
         </div>
      </form>
      <div class="searchContent col-12 row"></div>`);
    bind_plavSearch();
})

$('#controlBtns').delegate('#openShipInput', "click", function () {
    $('#content').html(
        `<form name="shipForm" class="col-12 shipForm">
         <div class="row">
            <div class="col-9 ui-widget">
               <label for="shipValues">Отгрузить</label>
               <input class="form-control col-10" name="shipValues" id="shipValues" autocomplete="off"/>
            </div>
            <div class="col-3 text-center">
               Вес: <span class="massInfo">0</span>
            </div>
         </div>
         <div class="row mb-3">
             <label for="account">Исключить</label>
             <input type="text" class="form-control" name="except" id="except">
         </div>
         <div class="row">
            <button type="button" class="btn btn-outline-info btn-block mb-2 ship" id="ship">Отгрузить</button>
         </div>
      </form>
      <div class="shipContent col-12 row"></div>`);
    bind_ship();
})

$('#controlBtns').delegate('#openShipHistory', "click", function () {
    historyTable = '';
    $.ajax(
        {
            type: 'GET',
            url: `http://ferro-trade.ru/api/history/all`,
            dataType: 'json',
            async: false,
            success: function (data, textStatus) {
                console.log(data);
                historyTable += table_for_history(data);
                $('#content').html(historyTable);
            }
        }
    )
})

$('#controlBtns').delegate('#hide', 'click', function () {
    $('#cards').html('');
    $('#hide').attr('class', 'btn btn-outline-info btn-block mb-2 hide d-none');
})

$('#controlBtns').delegate("#add", "click", function () {
    console.log("dlkf ");
    $('.form').css('display', 'inline');
});

$('#controlBtns').delegate("#addMany", "click", function () {
    console.log("dlkf ");
    $('.addManyForm').css('display', 'inline');
});

$('#controlBtns').delegate("#update", "click", function () {
    $(".updateForm").css('display', 'inline');
});

$('#content').delegate("#switcher", "click", function () {
    if (!$("#switcher")[0].checked) {
        $.ajax(
            {
                type: 'GET',
                url: 'http://5.200.47.32:80/api/table',
                dataType: 'json',
                success: function (data, textStatus) {
                    console.log(data);
                    products = table(data);
                    $('#cards').html(products);
                }
            }
        )
    }
    else {
        products = "";
        $.ajax(
            {
                type: 'GET',
                url: 'http://5.200.47.32:80/api/positions',
                dataType: 'json',
                success: function(data, textStatus)
                {
                    console.log(data);
                    products = cards(data);
                    $('#cards').html(products);
                }
            }
        )
    }
});

$('.form').delegate('.close-block', 'click', function () {
    $('.form').fadeOut();
})

$('.addManyForm').delegate('.close-block-regMany', 'click', function () {
    $('.addManyForm').fadeOut();
})

$('.updateForm').delegate('.close-block-update', 'click', function () {
    $('.updateForm').fadeOut();
})

$('#content').delegate('#search', "click", function () {
    products = "";
    val = document.forms.searchForm.searchValue.value;
    console.log(val);
    ssyl = `http://5.200.47.32/api/search/${val}`
    $.ajax(
        {
            type: 'GET',
            url: ssyl,
            dataType: 'json',
            success: function (data, textStatus) {
                console.log(data);
                data.forEach(function (elem) {
                    if (elem.status == 'Departured')
                        status = 'Отгружен';
                    else if (elem.status == 'In_stock')
                        status = 'На складе';
                    if (elem.type == 'POSITION') {
                        if (elem.comment == null || elem.comment == "") {
                            products = products + card_without_comm(elem.id, elem.mark, elem.diameter, elem.packing, elem.part, elem.plav, elem.mass.toFixed(2), status);
                        } else {
                            products = products + card(elem.id, elem.mark, elem.diameter, elem.packing, elem.comment, elem.part, elem.plav, elem.mass.toFixed(2), status);
                        }
                    } else {
                        if (elem.comment == null || elem.comment == "") {
                            products = products + pack_without_comm(elem.id, elem.mark, elem.diameter, elem.packing, elem.part, elem.plav, elem.mass.toFixed(2), status);
                        } else {
                            products = products + pack(elem.id, elem.mark, elem.diameter, elem.packing, elem.comment, elem.part, elem.plav, elem.mass.toFixed(2), status);
                        }
                    }
                });
                $('.searchContent').html(products);
            },
            error: function () {
                console.log(data);
                profucts = data;
            }
        }
    );
    console.log(products);
})

$("#content").delegate("#unite", 'click', function(){
    $.ajax(
        {
            type: "GET",
            url: `http://5.200.47.32/api/union?ids=${$("#uniteValues").val()}`,
            success: function(data, textStatus)
            {
                console.log(data);
            },
            error: function(data, textStatus)
            {
                console.log(data);
                if (data.status == 500){
                    $(".error2").css('display', 'inline');
                }
            }
        })
})

$('#content').delegate('#ship', 'click', function(){
    console.log($("#shipValues").is(":focus"));
    request = document.forms.shipForm.shipValues.value;
    except = $("#except").val();
    $.ajax({
        type: 'POST',
        url: `http://5.200.47.32:80/api/departure?request=${request}&except=${except}`,
        dataType: 'json',
        success: function(data, textStatus)
        {
            console.log(data);
            products = '';
            data.forEach(function(elem){
                if (elem.status == 'Departured')
                    status = 'Отгружен';
                else if (elem.status == 'In_stock')
                    status = 'На складе';
                products += card_depart(elem.id, elem.mark, elem.diameter, elem.packing, elem.part, elem.plav, elem.mass, status);
            })
            form = `<div class="row departureForm col-12">
         <div class="col-md-12">
             <form class="departureForm text-center" name="departureForm">
                 <div class="row">
                     <label for="contrAgent">Контр. агент</label>
                     <input type="text" class="form-control" name="contrAgent" id="contrAgent">
                 </div>
                 <div class="row">
                     <label for="account">Счет</label>
                     <input type="text" class="form-control" name="account" id="account">
                 </div>
             </form>
             <button class="btn btn-outline-success btn-block" style="margin-top: 5px;" id="depart">Отгрузить</button>
         </div>
     </div><div class='cards col-12 row'>`;
            products = form + products + '</div>';
            $('#content').html(products);
            data.forEach(function(elem){
                console.log(elem);
                $(`#mass,#${elem.id} input`).val(`${elem.mass}`);
            })
        }
    })
})

$('#content').delegate('#depart', 'click', function () {
    $("#depart").prop("disabled", true);
    contrAgent = document.forms.departureForm.contrAgent.value;
    account = document.forms.departureForm.account.value;
    objects = [];
    cards_mass = $('.mass');
    cards_id = $('.card');
    for (i = 0; i < cards_id.length; i++) {
        objects.push({
            "id": $(cards_id[i]).attr('id'),
            "weight": parseFloat(cards_mass[i].value.replaceAll(',', '.'))
        })
    }
    console.log(objects, contrAgent, account);
    object = {
        "data": objects,
        "contrAgent": contrAgent,
        "account": account
    }
    $.ajax({
        type: 'POST',
        url: `http://5.200.47.32:80/api/departureConfirmation`,
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(object),
        dataType: 'json',
        success: function (data, textStatus) {
            window.open(`http://www.ferro-trade.ru/api/files/${data.file}`, "_new");
            console.log(data);
            objs = [];
            data.id.forEach(function (elem) {
                $.ajax({
                    type: 'GET',
                    url: `http://5.200.47.32:80/api/position/${elem}`,
                    dataType: 'json',
                    async: false,
                    success: function (data, textStatus) {
                        console.log(data);
                        objs.push(data);
                    }
                })
            })
            console.log(objs);
            products = '';
            objs.forEach(function (obj) {
                products += card_to_print(obj.id);
            })
            $("#depart").prop("disabled", false);
            $('#content').html(products);
        }
    })
})

function CodetoPrint(id) {
    str = '';
    gost = '1';
    $.ajax({
        type: 'GET',
        url: `http://5.200.47.32/api/position/${id}`,
        dataType: 'json',
        async: false,
        success: function (data, textStatus) {
            console.log(data);
            mark = data.mark.replaceAll('–', '-');
            $.ajax({
                type: 'GET',
                url: `http://5.200.47.32/api/gost?mark=${mark}`,
                async: false,
                success: function (data, textStatus) {
                    console.log(data);
                    gost = data;
                },
                error: function (data, textStatus) {
                    console.log(data);
                }
            })
            str = "<html><title>Штрих-код</title><head><scri" +
                "pt>function step1(){\n" +
                "setTimeout('step2()', 10);}\n" +
                "function step2(){window.print();window.close()}\n" +
                "</scri" + "pt></head><body onload='step1()'>\n" +
                "<img width='350' height='100' style='margin: auto;' src='" +
                `http://5.200.47.32/api/code/${id}` + `' />
            <p style='font-size: 20px; margin: 0'>Производитель: ${data.manufacturer}</p>
            <p style='font-size: 20px; margin: 0'>Марка: ${data.mark}</p>
            <p style='font-size: 20px; margin: 0'>${gost}</p>
            <p style='font-size: 20px; margin: 0'>Диаметр: ${data.diameter}</p>
            <p style='font-size: 20px; margin: 0'>Вид поставки: ${data.packing}</p>
            <p style='font-size: 20px; margin: 0'>Партия: ${data.part}</p>
            <p style='font-size: 20px; margin: 0'>Плавка: ${data.plav}</p>
            <p style='font-size: 20px; margin: 0'>Масса: ${data.mass}</p>
</body></html>`;
            console.log(str);
        },
        error: function (data, textStatus) {
            console.log(data);
        }
    })
    console.log(str);
    return str;
}

function PrintCode(id) {
    console.log(arguments);
    prints = '';
    for (i = 0; i < arguments.length; i++) {
        prints += CodetoPrint(arguments[i]);
        console.log(prints);
    }
    var Pagelink = "";
    var pwa = window.open(Pagelink, "_blank");
    pwa.document.open();
    pwa.document.write(prints);
    pwa.document.close();
}

function PackageToPrint(id) {
    str = '';
    gost = '1';
    $.ajax({
        type: 'GET',
        url: `http://5.200.47.32/api/package?id=${id}`,
        dataType: 'json',
        async: false,
        success: function (data, textStatus) {
            console.log(data);
            mark = data.mark.replaceAll('–', '-');
            $.ajax({
                type: 'GET',
                url: `http://5.200.47.32/api/gost?mark=${mark}`,
                async: false,
                success: function (data, textStatus) {
                    console.log(data);
                    gost = data;
                },
                error: function (data, textStatus) {
                    console.log(data);
                }
            })
            str = "<html><title>Штрих-код</title><head><scri" +
                "pt>function step1(){\n" +
                "setTimeout('step2()', 10);}\n" +
                "function step2(){window.print();window.close()}\n" +
                "</scri" + "pt></head><body onload='step1()'>\n" +
                "<img width='350' height='100' style='margin: auto;' src='" +
                `http://5.200.47.32/api/code/${id}` + `' />
            <p style='font-size: 20px; margin: 0'>Производитель: ${data.manufacturer}</p>
            <p style='font-size: 20px; margin: 0'>Марка: ${data.mark}</p>
            <p style='font-size: 20px; margin: 0'>${gost}</p>
            <p style='font-size: 20px; margin: 0'>Диаметр: ${data.diameter}</p>
            <p style='font-size: 20px; margin: 0'>Вид поставки: ${data.packing}</p>
            <p style='font-size: 20px; margin: 0'>Партия: ${data.part}</p>
            <p style='font-size: 20px; margin: 0'>Плавка: ${data.plav}</p>
            <p style='font-size: 20px; margin: 0'>Масса: ${data.mass.toFixed(2)}</p>
</body></html>`;
            console.log(str);
        },
        error: function (data, textStatus) {
            console.log(data);
        }
    })
    console.log(str);
    return str;
}

function PrintPackage(id) {
    console.log(arguments);
    prints = '';
    for (i = 0; i < arguments.length; i++) {
        prints += PackageToPrint(arguments[i]);
        console.log(prints);
    }
    var Pagelink = "";
    var pwa = window.open(Pagelink, "_blank");
    pwa.document.open();
    pwa.document.write(prints);
    pwa.document.close();
}

function sorting() {
    console.log($('#sort option:selected').val());
    sort_data = [];
    $.ajax(
        {
            type: 'GET',
            url: 'http://5.200.47.32/api/positions',
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
                    if (product.status == 'Departured')
                        status = 'Отгружен';
                    else if (product.status == 'In_stock')
                        status = 'На складе';
                    if (product.comment == null || product.comment == "") {
                        products = products + card_without_comm(product.id, product.mark, product.diameter, product.packing, product.part, product.plav, product.mass, status);
                    } else {
                        products = products + card(product.id, product.mark, product.diameter, product.packing, product.comment, product.part, product.plav, product.mass, status);
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
            url: `http://5.200.47.32/api/search/manufacturer/-`,
            dataType: 'json',
            success: function (data, textStatus) {
                console.log(data);
                $("#manufacturer").autocomplete({
                    source: data
                });
                $("#manufacturerMany").autocomplete({
                    source: data
                });
            }
        })
    } else {
        $.ajax({
            type: 'GET',
            url: `http://5.200.47.32/api/search/manufacturer/${input}`,
            dataType: 'json',
            success: function (data, textStatus) {
                console.log(data);
                $("#manufacturer").autocomplete({
                    source: data
                });
                $("#manufacturerMany").autocomplete({
                    source: data
                });
            }
        })
    }
}

$(document).on("keypress", 'form', function (e) {
    var code = e.keyCode || e.which;
    if (code == 13) {
        e.preventDefault();
        return false;
    }
});

function fillFilter(){
    var availableTags = ["Моток",
        "К200",
        "Д200",
        "К300",
        "Д300",
        "К415",
        "Д415"]
    $.ajax({
        type: 'GET',
        url: `http://5.200.47.32:80/api/position/marks`,
        dataType: 'json',
        success: function(data, textStatus)
        {
            console.log(data);
            tags = '';
            data.forEach(function(tag){
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
        url: `http://5.200.47.32:80/api/position/diameter`,
        dataType: 'json',
        success: function(data, textStatus)
        {
            console.log(data);
            $("#slider").slider({
                animate: "slow",
                range: true,
                min: parseFloat(data[0]),
                max: parseFloat(data[1]),
                values: [parseFloat(data[0]), parseFloat(data[1])],
                step: 0.1,
                slide : function(event, ui) {
                    $("#result-slider").text( "от " + ui.values[ 0 ] + " до " + ui.values[ 1 ] );
                }
            });
            $( "#result-slider" ).text("от " + $("#slider").slider("values", 0) + " до " + $("#slider").slider("values", 1));
        }
    })
    tags = '';
    availableTags.forEach(function(tag){
        tags += `<input name="packingType" class="form-check-input" type="checkbox" id="${tag}" value="${tag}">
      <label class="form-check-label" for="${tag}">
      ${tag}
      </label><div class="w-100"></div>`;
    })
    $('.packing').html(tags);

    $('.filter').delegate('#filtrate', 'click', function(){
        selected_marks = [];
        selected_packings = [];
        $("input:checkbox[name=markType]:checked").each(function(){
            selected_marks.push($(this).val());
        });
        $("input:checkbox[name=packingType]:checked").each(function(){
            selected_packings.push($(this).val());
        });
        min = $("#slider").slider("values", 0);
        max = $("#slider").slider("values", 1);
        object = {
            "mark": selected_marks,
            "packing": selected_packings,
            "DL": min,
            "DM": max,
            "table": $("#switcher")[0].checked
        }
        console.log(!$("#switcher")[0].checked);
        if (!$("#switcher")[0].checked){
            $.ajax({
                type: 'POST',
                url: `http://5.200.47.32:80/api/filter/table`,
                contentType: "application/json; charset=utf-8",
                data: JSON.stringify(object),
                dataType: 'json',
                cache: false,
                success: function(data, textStatus)
                {
                    console.log(data);
                    products = table(data);
                    $("#cards").html(products);
                },
                error: function(data, textStatus)
                {
                    console.log(data);
                }
            });
        }
        else{
            $.ajax({
                type: 'POST',
                url: `http://5.200.47.32:80/api/filter`,
                contentType: "application/json; charset=utf-8",
                data: JSON.stringify(object),
                dataType: 'json',
                cache: false,
                success: function(data, textStatus)
                {
                    console.log(data);
                    products = cards(data);
                    $("#cards").html(products);
                },
                error: function(data, textStatus)
                {
                    console.log(data);
                }
            })
        }
    })
}

function bind_search() {
    $('#searchValue').keyup(function (event) {
        elems = document.querySelectorAll('#search');
        console.log('yep');
        if (event.keyCode == 13 && elems.length != 0) {
            $('#search').click();
            $(this).val('');
        }
        return false;
    })
}

function bind_plavSearch() {
    $('#plavSearchValue').keyup(function (event) {
        elems = document.querySelectorAll('#plavSearch');
        console.log('yep');
        if (event.keyCode == 13 && elems.length != 0) {
            $('#plavSearch').click();
            $(this).val('');
        }
        return false;
    })

    $('#content').unbind('click').delegate('#plavSearch', "click", function () {
        val = document.forms.plavSearchForm.plavSearchValue.value;
        console.log(val);
        ssyl = `http://ferro-trade.ru/api/search/plavka?plav=${val}`
        $.ajax(
            {
                type: 'POST',
                url: ssyl,
                dataType: 'json',
                success: function (data, textStatus) {
                    console.log(data);
                    plavTable = table(data);
                    $("#content").html(plavTable);
                },
                error: function (data, textStatus) {
                    console.log(data);
                }
            }
        );
    })
}

function bind_ship() {
    $('#shipValues').keyup(function (event) {
        elems = document.querySelectorAll('#ship');
        console.log($("#shipValues").is(':focus'));
        if (event.keyCode == 13 && elems.length != 0 && $("#shipValues").is(':focus')) {
            $(this).val($(this).val() + ', ');
            console.log($(this).val());
            $.ajax({
                type: 'GET',
                url: `http://5.200.47.32/api/departure?query=${$(this).val()}`,
                success: function(data, textStatus){
                    console.log(data.toFixed(2));
                    $(".massInfo").html(data.toFixed(2));
                }
            })
        }
        return false;
    })
    $('#except').keyup(function(event){
        elems = document.querySelectorAll('#ship');
        console.log($("#except").is(':focus'));
        if (event.keyCode == 13 && elems.length != 0 && $("#except").is(':focus')){
            $(this).val($(this).val() + ', ');
        }
        return false;
    })
}

function bind_unite(){
    $('#uniteValues').keyup(function(event){
        elems = document.querySelectorAll('#unite');
        if (event.keyCode == 13 && elems.length != 0){
            $(this).val($(this).val() + ', ');
        }
        return false;
    })
}

function bind_depart() {
    $('#departValues').keyup(function (event) {
        elems = document.querySelectorAll('#depart');
        if (event.keyCode == 13 && elems.length != 0) {
            $(this).val($(this).val() + ', ');
        }
        return false;
    })
}

function addManyOnChange() {
    mass = 0;
    $(".regManyMass").each(function () {
        cur = parseFloat($(this).val().replaceAll(',', '.'));
        if (!isNaN(cur)) {
            mass += cur;
        }
        console.log($(this).val());
        console.log($(this).val().replaceAll(',', '.'));
        console.log(parseFloat($(this).val().replaceAll(',', '.')));
    });
    console.log(mass);
    $(".addManyMass").html(mass);
}

function openShipmentHistory(id) {
    window.open(`shipment_history?id=${id}`, "_blank");
}