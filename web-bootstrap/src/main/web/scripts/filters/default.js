jaxrsDoc.filter('default', function() {
  return function(input, defaultVal) {
	if (typeof input == 'undefined' || !input) {
		return defaultVal;
	}
	return input;
  };
});
