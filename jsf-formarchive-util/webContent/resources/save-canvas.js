/**
 * MARP-3812:
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
  var textareaReplacements = [];

  // Process all textarea elements on the page
  document.querySelectorAll('textarea').forEach(function(textareaElement) {
    if (textareaElement.offsetParent === null) {
      return;
    }
    var textareaBounds = textareaElement.getBoundingClientRect();
    // Get all computed CSS styles
    var style = window.getComputedStyle(textareaElement);

    // Create replacement div with same text
    var overlayDiv = document.createElement('div');
    overlayDiv.textContent = textareaElement.value;

    // Copy all computed styles to preserve appearance
    for (var i = 0; i < style.length; i++) {
      var prop = style[i];
      overlayDiv.style[prop] = style.getPropertyValue(prop);
    }

    overlayDiv.style.position = 'absolute';
    overlayDiv.style.top = (textareaBounds.top + window.scrollY) + 'px';
    overlayDiv.style.left = (textareaBounds.left + window.scrollX) + 'px';
    overlayDiv.style.margin = '0';
    overlayDiv.style.whiteSpace = 'pre-wrap';
    overlayDiv.style.overflow = 'hidden';
    overlayDiv.style.pointerEvents = 'none';
    document.body.appendChild(overlayDiv);
    textareaElement.style.visibility = 'hidden';
    textareaReplacements.push({ textareaElement: textareaElement, overlayDiv: overlayDiv });
  });

  html2canvas(document.body).then(function(canvas) {
    var encodedImage = canvas.toDataURL("image/png");
    document.querySelector('[id$=":save-canvas-form-content"]').value = encodedImage;
    submitCanvasSnapRC();
  }).finally(function() {
    // Restore original textareas and remove replacement divs
    textareaReplacements.forEach(function(textareaReplacement) {
      textareaReplacement.textareaElement.style.visibility = '';
      textareaReplacement.overlayDiv.remove();
    });

  });
}