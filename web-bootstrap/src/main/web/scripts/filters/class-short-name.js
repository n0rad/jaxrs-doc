jaxrsDoc.filter('classShortName', function() {
  return function(input) {
    return input.substring(input.lastIndexOf('.') + 1);
  };
});
