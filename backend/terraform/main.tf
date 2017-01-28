// Configure the Google Cloud provider
provider "google" {
  credentials = "${file("kcl-chit-chat-56771595d3c8.json")}"
  project     = "kcl-chit-chat"
  region      = "eu-west1"
}

// For build artifacts
/**
 * Directory structure:
 * /builds/
 * /builds/BUILD_#/backend/coverage
 * /builds/BUILD_#/java_client/coverage
 * /builds/BUILD_#/android_client/coverage
 *
 * /releases/
 */
resource "google_storage_bucket" "build-artifacts" {
  name     = "kcl-chit-chat-artifacts"
  location = "EU"
  force_destroy = true
  storage_class = "STANDARD"

  website {
    main_page_suffix = "index.html"
    not_found_page = "404.html"
  }
}
