$(document).ready(function () {

    $('#category').change(function(){
        const category = $('#category').val().toLowerCase();
        $('#categoryInput').attr('value', category);
        $('#categoryForm').submit();
        console.log(category);
    });

    $('#changePhoto').click(function() {
            if ($('#changePhoto').html() === 'Change') {
                $('#changePhoto').html('Cancel');
                $('#photo').hide();
                $('#photoInput').removeAttr('hidden')
                $('#file').attr('name', 'file')
            } else {
                $('#changePhoto').html('Change');
                $('#photo').show();
                $('#photoInput').attr('hidden', 'hidden');
                $('#file').attr('name', '')
            }
    });

    $('#addIngredient').click(function() {
        var currentIndex = $('.ingredientCount').length
        console.log(currentIndex);
        var ingredientHTML = `
            <div class="ingredientCount"></div>

            <div class="grid-20"><p class="label-spacing"></p></div>

            <div class="grid-30">
            <p class="label-spacing">
                <input type="text" name="ingredients[${currentIndex}].item"  />
            </p>
            </div>

            <div class="grid-30">
            <p class="label-spacing">
                <input type="text" name="ingredients[${currentIndex}].condition" />
            </p>
            </div>

            <div class="grid-20">
            <p class="label-spacing">
                <input type="text" name="ingredients[${currentIndex}].quantity" />
            </p>
            </div>
         `;
        $('#ingredientsList').append(ingredientHTML);
    });

    $('#addStep').click(function() {
        var currentIndex = $('.stepCount').length;
        var counter = currentIndex + 1;
        var stepHTML = `
            <div class="stepCount"></div>

            <div class="prefix-20 grid-80">
                <p class="label-spacing">
                ${counter}.
                  <input type="text" name="steps[${currentIndex}]"/>
                </p>
            </div>
        `;
        $('#stepList').append(stepHTML);
    });

    $('#signup').click(function() {
        var password1 = $('#password1').val();
        var password2 = $('#password2').val();
        if (password1 === password2) {
            $('#signupForm').submit();
        } else {
            console.log('passwords do not match');
        }
    });

});



function closeFlash() {
    var flash = document.getElementById('flash-message');
    flash.style.display = 'none';
}

