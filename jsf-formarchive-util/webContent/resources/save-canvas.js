/**
 * Saves a screenshot of the current page using html2canvas.
 *
 * To fix html2canvas limitation: textarea content cannot be renderred correctly, 
 * replace each visible textarea with a div that:
 * - contains the same text
 * - copies all computed CSS styles
 * - is positioned exactly over the textarea
 *
 * After the screenshot is taken, the original textareas are restored.
 */
function saveCanvas() {
  // Store original textarea and their replacement divs
  var replacements = [];

  // Process all textarea elements on the page
  document.querySelectorAll('textarea').forEach(function(ta) {
    if (ta.offsetParent === null) {
      return;
    }
    var rect = ta.getBoundingClientRect();
    // Get all computed CSS styles
    var style = window.getComputedStyle(ta);

    // Create replacement div with same text
    var div = document.createElement('div');
    div.textContent = ta.value;

    // Copy all computed styles to preserve appearance
    for (var i = 0; i < style.length; i++) {
      var prop = style[i];
      div.style[prop] = style.getPropertyValue(prop);
    }

    div.style.position = 'absolute';
    div.style.top = (rect.top + window.scrollY) + 'px';
    div.style.left = (rect.left + window.scrollX) + 'px';
    div.style.margin = '0';
    div.style.whiteSpace = 'pre-wrap';
    div.style.overflow = 'hidden';
    div.style.pointerEvents = 'none';
    document.body.appendChild(div);
    ta.style.visibility = 'hidden';
    replacements.push({ ta: ta, div: div });
  });

  html2canvas(document.body).then(function(canvas) {
    var encodedimg = canvas.toDataURL("image/png");
    document.querySelector('[id$=":save-canvas-form-content"]').value = encodedimg;
    submitCanvasSnapRC();
  }).finally(function() {
        // Restore original textareas and remove replacement divs
    replacements.forEach(function(replacement) {
      replacement.ta.style.visibility = '';
      replacement.div.remove();
    });

  });
}