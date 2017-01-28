// Configure the Google Cloud provider
provider "google" {
  credentials = "${file("kcl-chit-chat-ee111c7c3199.json")}"
  project     = "kcl-chit-chat"
  region      = "eu-west1"
}

// For build artifacts
/**
 * Directory structure:
 * /builds/
 * /builds/BUILD_#/backend/...
 * /builds/BUILD_#/java_client/...
 * /builds/BUILD_#/android_client/...
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

resource "google_storage_bucket_object" "artifacts-index" {
  name   = "index.html"
  source = "artifacts.html"
  bucket = "kcl-chit-chat-artifacts"
}

resource "google_storage_object_acl" "image-store-acl" {
  bucket = "${google_storage_bucket.build-artifacts.name}"
  object = "${google_storage_bucket_object.artifacts-index.name}"

  role_entity = [
    "READER:allUsers",
  ]
}
