{
	description = "Minecraft Fabric mod that prevents players from escaping combat by logging out";

	inputs = {
		nixpkgs.url = "github:NixOS/nixpkgs/nixos-26.05";
		flake-utils.url = "github:numtide/flake-utils";
	};

	outputs = {
		nixpkgs,
		flake-utils,
		...
	}: flake-utils.lib.eachDefaultSystem (system:
			let
				pkgs = nixpkgs.legacyPackages.${system};
			in {
				devShell = pkgs.mkShell {
					nativeBuildInputs = with pkgs; [
						gradle
						openjdk21
					];

					env = {
						LD_LIBRARY_PATH = pkgs.lib.makeLibraryPath (with pkgs; [
							libGL
							glfw3-minecraft
							libpulseaudio
							flite
							udev
						]);
					};
				};
			});
}
